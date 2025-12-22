package org.yann.verulia.framework.auth.strategy;

import org.yann.verulia.framework.auth.domain.AuthUser;
import org.yann.verulia.framework.auth.domain.LoginBody;
import org.yann.verulia.framework.auth.domain.LoginConfig;

/**
 * 认证策略接口
 *
 * @author Yann
 */
public interface IAuthStrategy {

    /**
     * 获取登录类型
     *
     * @return 登录类型 (e.g., "password", "sms")
     */
    String getLoginType();

    /**
     * 执行认证
     * @param loginBody 认证参数
     * @return 认证后用户信息
     */
    AuthUser authenticate(LoginBody loginBody);

    /**
     * 获取登录配置
     * 返回 null 代表使用系统默认配置
     */
    default LoginConfig getLoginConfig() {
        return null;
    };
}
