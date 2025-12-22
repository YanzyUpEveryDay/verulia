package org.yann.verulia.framework.security.service;


import cn.dev33.satoken.stp.StpUtil;
import org.yann.verulia.framework.core.service.SecurityContext;

/**
 *
 * @author Yann
 * @date 2025/12/16 11:36
 */
public class SaTokenSecurityContext implements SecurityContext {

    @Override
    public Long getUserId() {
        if (StpUtil.isLogin()) {
            return StpUtil.getLoginIdAsLong();
        }
        return null;
    }

    @Override
    public String getUsername() {
        return "";
    }
}
