package org.yann.verulia.framework.security.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yann.verulia.framework.core.domain.R;
import org.yann.verulia.framework.core.enums.ResultCode;

/**
 * 安全模块全局异常处理器
 * 配合 Web 模块的 GlobalExceptionHandler 使用
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class SecurityExceptionHandler {

    /**
     * 未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public R<Void> handleNotLoginException(NotLoginException e) {
        log.warn("用户未登录或Token失效: {}", e.getMessage());
        return R.fail(ResultCode.UN_AUTHORIZED);
    }

    /**
     * 无权限异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public R<Void> handleNotPermissionException(NotPermissionException e) {
        log.warn("无权限访问: {}", e.getMessage());
        return R.fail(ResultCode.FAILURE.getCode(), "无此权限：" + e.getCode());
    }

    /**
     * 无角色异常
     */
    @ExceptionHandler(NotRoleException.class)
    public R<Void> handleNotRoleException(NotRoleException e) {
        log.warn("无角色访问: {}", e.getMessage());
        return R.fail(ResultCode.FAILURE.getCode(), "无此角色：" + e.getRole());
    }
}