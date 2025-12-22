package org.yann.verulia.system.service.strategy;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.v7.core.util.RandomUtil;
import cn.hutool.v7.http.HttpUtil;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.yann.verulia.framework.auth.domain.AuthUser;
import org.yann.verulia.framework.auth.domain.LoginBody;
import org.yann.verulia.framework.auth.exception.AuthException;
import org.yann.verulia.framework.auth.strategy.IAuthStrategy;
import org.yann.verulia.system.domain.entity.SysSocialAuth;
import org.yann.verulia.system.domain.entity.SysUser;
import org.yann.verulia.system.event.LoginLogEvent;
import org.yann.verulia.system.mapper.SysSocialAuthMapper;
import org.yann.verulia.system.mapper.SysUserMapper;

/**
 * 微信小程序静默登录策略
 *
 * @author Yann
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MiniappAuthStrategy implements IAuthStrategy {

    private final WxMaService wxMaService;
    private final SysUserMapper sysUserMapper;
    private final SysSocialAuthMapper sysSocialAuthMapper;

    @Override
    public String getLoginType() {
        return "miniapp";
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

        SysSocialAuth socialAuth = sysSocialAuthMapper.selectOne(new LambdaQueryWrapper<SysSocialAuth>()
                .eq(SysSocialAuth::getSource, "miniapp")
                .eq(SysSocialAuth::getOpenid, openid));
        if (socialAuth != null) {
            socialAuth.getUserId();
        } else {
            registerAndBind(openid);
        }

        return null;
    }

    private Long registerAndBind(String openid) {
        SysUser user = new SysUser();
        user.setUsername("wx_" + openid.substring(0, 8));
        user.setNickname("微信用户");
        user.setPassword("");
        user.setStatus(0);
        sysUserMapper.insert(user);

        SysSocialAuth auth = new SysSocialAuth();
        auth.setUserId(user.getId());
        auth.setOpenid(openid);
        auth.setSource("miniapp");
        sysSocialAuthMapper.insert(auth);
        return user.getId();
    }
}
