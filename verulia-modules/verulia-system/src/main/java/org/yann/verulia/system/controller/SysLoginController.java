package org.yann.verulia.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yann.verulia.framework.core.domain.R;
import org.yann.verulia.framework.core.service.SecurityContext;
import org.yann.verulia.system.domain.dto.LoginBody;
import org.yann.verulia.system.domain.vo.LoginVo;
import org.yann.verulia.system.service.ISysLoginService;

/**
 * 登录 Controller
 *
 * @author Yann
 */
@RestController
@RequiredArgsConstructor
public class SysLoginController {

    private final ISysLoginService sysLoginService;

    private final SecurityContext securityContext;

    /**
     * 登录
     */
    @PostMapping("/login")
    public R<LoginVo> login(@RequestBody LoginBody loginBody) {
        return R.ok(sysLoginService.login(loginBody));
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/getRemoteInfo")
    public R<LoginVo> getRemoteInfo() {
        return R.ok(sysLoginService.getRemoteInfo(securityContext.getUserId()));
    }
}
