package org.yann.verulia.framework.core.service;


/**
 *
 * @author Yann
 * @date 2025/12/16 11:35
 */
public interface SecurityContext {

    /**
     * 获取当前登录用户ID
     */
    Long getUserId();

    /**
     * 获取当前登录用户名
     */
    String getUsername();

    /**
     * 获取当前登录用户类型
     */
    String getUserType();

    /**
     * 是否为会员
     */
    boolean isMember();
}
