package org.yann.verulia.framework.core.domain;

import lombok.Getter;
import lombok.Setter;
import org.yann.verulia.framework.core.enums.IResultCode;
import org.yann.verulia.framework.core.enums.ResultCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应实体
 * * @author Yann
 */
@Getter
@Setter
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 数据对象
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp;

    private R() {
        this.timestamp = System.currentTimeMillis();
    }

    // ============================ 成功响应 ============================

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        return ok(ResultCode.SUCCESS, data);
    }

    public static <T> R<T> ok(IResultCode resultCode, T data) {
        return build(resultCode.getCode(), resultCode.getMsg(), data);
    }

    // ============================ 失败响应 ============================

    public static <T> R<T> fail(String msg) {
        return build(ResultCode.FAILURE.getCode(), msg, null);
    }

    public static <T> R<T> fail(IResultCode resultCode) {
        return build(resultCode.getCode(), resultCode.getMsg(), null);
    }

    public static <T> R<T> fail(Integer code, String msg) {
        return build(code, msg, null);
    }

    // ============================ 构建方法 ============================

    private static <T> R<T> build(Integer code, String msg, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    // ============================ 扩展：链式调用 ============================

    /**
     * 允许自定义修改 code，例如：R.ok().setCode(201)
     */
    public R<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    /**
     * 允许自定义修改 msg
     */
    public R<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}