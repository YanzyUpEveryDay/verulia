package org.yann.verulia.framework.security.service;


import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import org.yann.verulia.framework.core.service.SecurityContext;
import org.yann.verulia.framework.security.StpKit;

/**
 *
 * @author Yann
 * @date 2025/12/16 11:36
 */
public class SaTokenSecurityContext implements SecurityContext {

    @Override
    public Long getUserId() {
        if (StpKit.DEFAULT.isLogin()) {
            return StpKit.DEFAULT.getLoginIdAsLong();
        }
        if (StpKit.MEMBER.isLogin()) {
            return StpKit.MEMBER.getLoginIdAsLong();
        }
        throw new NotLoginException("用户未登录或Token失效", StpUtil.TYPE, StpUtil.TYPE);
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public String getUserType() {
        if (StpKit.DEFAULT.isLogin()) return "system";
        if (StpKit.MEMBER.isLogin()) return "member";
        return "";
    }

    @Override
    public boolean isMember() {
        return StpKit.MEMBER.isLogin();
    }
}
