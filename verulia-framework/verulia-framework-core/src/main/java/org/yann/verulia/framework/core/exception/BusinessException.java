package org.yann.verulia.framework.core.exception;

import java.io.Serial;


import lombok.Getter;
import org.yann.verulia.framework.core.enums.IResultCode;
import org.yann.verulia.framework.core.enums.ResultCode;

import java.io.Serial;

/**
 * 业务异常
 * <p>
 * 说明：
 * 1. 继承 RuntimeException，在 Service 层可直接抛出，无需 try-catch。
 * 2. 配合 GlobalExceptionHandler 使用，一旦抛出，前端会收到对应的 JSON 报错。
 *
 * @author Yann
 * @date 2025/12/15 13:59
 */
@Getter
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误提示
     */
    private final String msg;

    /**
     * 方式一：直接使用枚举（推荐）
     * 用法：throw new BusinessException(ResultCode.UN_AUTHORIZED);
     */
    public BusinessException(IResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    /**
     * 方式二：仅指定错误消息（使用默认的 400 失败码）
     * 用法：throw new BusinessException("库存不足");
     */
    public BusinessException(String msg) {
        super(msg);
        this.code = ResultCode.FAILURE.getCode();
        this.msg = msg;
    }

    /**
     * 方式三：完全自定义（极少使用，除非为了兼容第三方系统错误码）
     * 用法：throw new BusinessException(10086, "余额不足");
     */
    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
