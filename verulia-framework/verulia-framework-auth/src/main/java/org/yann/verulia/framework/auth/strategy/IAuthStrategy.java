package org.yann.verulia.framework.auth.strategy;

import org.yann.verulia.framework.auth.domain.LoginBody;

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
     *
     * @param loginBody 认证参数
     * @return 认证通过后的用户ID
     */
    Long authenticate(LoginBody loginBody);
}
