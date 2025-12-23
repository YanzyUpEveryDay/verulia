package org.yann.verulia.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.v7.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.yann.verulia.framework.auth.domain.AuthUser;
import org.yann.verulia.framework.auth.domain.LoginBody;
import org.yann.verulia.framework.auth.domain.LoginConfig;
import org.yann.verulia.framework.auth.strategy.AuthStrategyFactory;
import org.yann.verulia.framework.auth.strategy.IAuthStrategy;
import org.yann.verulia.framework.core.domain.R;
import org.yann.verulia.framework.core.service.SecurityContext;
import org.yann.verulia.system.domain.vo.LoginVo;
import org.yann.verulia.system.service.ISysLoginService;

/**
 * 登录
 * @author Yann
 * @date 2025/12/17 16:07
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthStrategyFactory authStrategyFactory;

    private final SecurityContext securityContext;

    private final ISysLoginService sysLoginService;

    /**
     * @param loginBody 请求参数
     * @return 响应token
     */
    @SaIgnore
    @PostMapping("/login")
    public R<String> login(@Validated @RequestBody LoginBody loginBody) {
        IAuthStrategy strategy = authStrategyFactory.getStrategy(loginBody.getGrantType());
        // 认证策略
        AuthUser authUser = strategy.authenticate(loginBody);
        // 获取登录配置
        LoginConfig config = strategy.getLoginConfig();
        SaLoginParameter parameter = null;
        if (ObjUtil.isNotNull(config)) {
            parameter = new SaLoginParameter()
                    .setDeviceType(config.getDevice())
                    .setTimeout(config.getTimeout() != null ? config.getTimeout() : SaTokenDao.NEVER_EXPIRE)
                    .setActiveTimeout(config.getActiveTimeout() != null ? config.getActiveTimeout() : SaTokenDao.NEVER_EXPIRE)
                    .setIsConcurrent(config.getIsConcurrent());
        }
        StpUtil.login(authUser.getUserId(), parameter);
        // 缓存用户信息
        StpUtil.getSession().set("user", authUser);
        return R.ok(StpUtil.getTokenValue());
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/getRemoteInfo")
    public R<LoginVo> getRemoteInfo() {
        // 获取登录用户状态
        Long userId = securityContext.getUserId();
        if (ObjUtil.isNotNull(userId)) {
            LoginVo remoteInfo = sysLoginService.getRemoteInfo(userId);
            return R.ok(remoteInfo);
        }
        return R.fail("未获取到用户登录状态");
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public R<Void> logout() {
        StpUtil.logout();
        return R.ok();
    }
}
