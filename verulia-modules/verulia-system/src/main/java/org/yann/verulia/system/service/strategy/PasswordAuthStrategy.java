package org.yann.verulia.system.service.strategy;

import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.yann.verulia.framework.auth.domain.AuthUser;
import org.yann.verulia.framework.auth.domain.LoginBody;
import org.yann.verulia.framework.auth.domain.LoginConfig;
import org.yann.verulia.framework.auth.exception.AuthException;
import org.yann.verulia.framework.auth.strategy.IAuthStrategy;
import org.yann.verulia.framework.core.exception.BusinessException;
import org.yann.verulia.system.domain.entity.SysUser;
import org.yann.verulia.system.event.LoginLogEvent;
import org.yann.verulia.system.mapper.SysUserMapper;

/**
 * 账号密码认证策略
 *
 * @author Yann
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordAuthStrategy implements IAuthStrategy {

    private final SysUserMapper sysUserMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public String getLoginType() {
        return "password";
    }

    @Override
    public AuthUser authenticate(LoginBody loginBody) {
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new AuthException("用户名或密码不能为空");
        }

        SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getUsername, username));

        if (user == null) {
            eventPublisher.publishEvent(new LoginLogEvent(this, username, false, "用户不存在"));
            throw new AuthException("用户不存在或密码错误");
        }

        if (!StrUtil.equals(SecureUtil.sha256(password), user.getPassword())) {
            eventPublisher.publishEvent(new LoginLogEvent(this, username, false, "密码错误"));
            throw new AuthException("用户不存在或密码错误");
        }

        if (user.getStatus() == 0) {
            eventPublisher.publishEvent(new LoginLogEvent(this, username, false, "账号已停用"));
            throw new AuthException("账号已停用");
        }

        eventPublisher.publishEvent(new LoginLogEvent(this, username, true, "登录成功"));
        return AuthUser.builder()
                .userId(user.getId())
                .username(username)
                .loginType(getLoginType())
                .build();
    }

    @Override
    public LoginConfig getLoginConfig() {
        return LoginConfig.builder()
                .device("PC")
                .timeout(60 * 60 * 2L)  // 两个小时
                .isConcurrent(true)
                .build();
    }
}
