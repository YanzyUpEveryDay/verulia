package org.yann.verulia.system.service.strategy;


import cn.hutool.v7.core.text.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.yann.verulia.framework.auth.domain.AuthUser;
import org.yann.verulia.framework.auth.domain.LoginBody;
import org.yann.verulia.framework.auth.exception.AuthException;
import org.yann.verulia.framework.auth.strategy.IAuthStrategy;
import org.yann.verulia.system.service.IAppMemberService;

/**
 *
 * @author Yann
 * @date 2025/12/25 16:00
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AppPasswordAuthStrategy implements IAuthStrategy {

    private IAppMemberService appMemberService;

    @Override
    public String getLoginType() {
        return "app-password";
    }

    @Override
    public AuthUser authenticate(LoginBody loginBody) {
        String mobile = loginBody.getUsername();
        String password = loginBody.getPassword();

        if (StrUtil.isBlank(mobile) || StrUtil.isBlank(password)) {
            throw new AuthException("用户名或密码不能为空");
        }

        // 根据手机号查询会员表



        return null;
    }
}
