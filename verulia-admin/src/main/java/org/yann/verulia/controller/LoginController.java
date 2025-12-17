package org.yann.verulia.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yann.verulia.framework.auth.strategy.AuthStrategyFactory;
import org.yann.verulia.framework.core.domain.R;
import org.yann.verulia.system.domain.vo.LoginVo;
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

    @PostMapping("/login")
    public R<LoginVo> login() {
        return R.ok();
    }
}
