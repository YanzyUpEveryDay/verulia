package org.yann.verulia.framework.core.enums;


import lombok.Getter;

/**
 * 状态码枚举
 *
 * @author Yann
 * @date 2025/12/15 13:29
 */
@Getter
public enum ResultCode implements IResultCode {
    SUCCESS(200, "操作成功"),
    FAILURE(400, "业务异常"),
    UN_AUTHORIZED(401, "请求未授权"),
    NOT_FOUND(404, "404 没找到请求"),
    MSG_NOT_READABLE(400, "消息不能读取"),
    INTERNAL_SERVER_ERROR(500, "服务器异常"),
    ;

    final Integer code;
    final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
