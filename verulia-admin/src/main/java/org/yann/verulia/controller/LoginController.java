package org.yann.verulia.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.yann.verulia.framework.auth.domain.LoginBody;
import org.yann.verulia.framework.auth.strategy.AuthStrategyFactory;
import org.yann.verulia.framework.auth.strategy.IAuthStrategy;
import org.yann.verulia.framework.core.domain.R;
import org.yann.verulia.system.service.ISysLoginService;

/**
 * 登录
 * @author Yann
 * @date 2025/12/17 16:07
 */
@SaIgnore
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final ISysLoginService sysLoginService;
    private final AuthStrategyFactory authStrategyFactory;

    /**
     * @param loginBody 请求参数
     * @return 响应token
     */
    @PostMapping("/login")
    public R<String> login(@Validated @RequestBody LoginBody loginBody) {
        IAuthStrategy strategy = authStrategyFactory.getStrategy(loginBody.getGrantType());
        Long useId = strategy.authenticate(loginBody);
        StpUtil.login(useId);
        return R.ok(StpUtil.getTokenValue());
    }
}
