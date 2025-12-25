package org.yann.verulia.system.service.strategy;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.yann.verulia.framework.auth.domain.AuthUser;
import org.yann.verulia.framework.auth.domain.LoginBody;
import org.yann.verulia.framework.auth.domain.LoginConfig;
import org.yann.verulia.framework.auth.exception.AuthException;
import org.yann.verulia.framework.auth.strategy.IAuthStrategy;
import org.yann.verulia.system.domain.dto.MemberDtos;
import org.yann.verulia.system.service.IAppMemberService;

/**
 * 微信小程序静默登录策略
 * <p>
 *     会员专属
 * </p>
 *
 * @author Yann
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AppMiniappAuthStrategy implements IAuthStrategy {

    private final WxMaService wxMaService;
    private final IAppMemberService appMemberService;

    @Override
    public String getLoginType() {
        return "app-miniapp";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthUser authenticate(LoginBody loginBody) {
        String code = loginBody.getCode();
        if (code == null || code.isBlank()) {
            throw new AuthException("认证Code不能为空");
        }

        WxMaJscode2SessionResult session;
        try {
            session = wxMaService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            log.error("微信登录调用失败: {}", e.getMessage());
            throw new AuthException("微信登录失败: " + e.getError().getErrorMsg());
        }

        String openid = session.getOpenid();
        if (openid == null) {
            throw new AuthException("微信登录失败: 未能获取OpenID");
        }

        // 验证会员是否已经注册
        MemberDtos.Result member = appMemberService.registerByWechat(new MemberDtos.WechatRegister(openid, session.getUnionid(), "微信用户", "", loginBody.getSource()));
        return AuthUser.builder()
                .userId(member.id())
                .loginType("miniapp")
                .username(member.nickname())
                .build();
    }

    @Override
    public LoginConfig getLoginConfig() {
        return LoginConfig.builder()
                .device("MiniApp")
                .timeout(60 * 60 * 24 * 30L)  // 30天
                .isConcurrent(false)
                .build();
    }
}
