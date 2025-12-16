package org.yann.verulia.framework.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.yann.verulia.framework.core.domain.R;
import org.yann.verulia.framework.core.enums.ResultCode;
import org.yann.verulia.framework.core.exception.BusinessException;

import java.util.Objects;

/**
 * 全局异常处理器
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有未知的异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常:", e);
        return R.fail(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "系统繁忙，请稍后重试");
    }

    /**
     * 处理自定义业务异常
     */
     @ExceptionHandler(BusinessException.class)
     public R<Void> handleBusinessException(BusinessException e) {
         return R.fail(e.getCode(), e.getMessage());
     }

    /**
     * 处理 404
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public R<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return R.fail(ResultCode.NOT_FOUND.getCode(), "路径不存在，请检查路径是否正确");
    }

    /**
     * 处理参数校验异常 (Spring Validation)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        log.warn("参数校验异常: {}", msg);
        return R.fail(ResultCode.FAILURE.getCode(), msg);
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        log.warn("参数绑定异常: {}", e.getMessage());
        return R.fail(ResultCode.FAILURE.getCode(), "参数格式错误");
    }
}