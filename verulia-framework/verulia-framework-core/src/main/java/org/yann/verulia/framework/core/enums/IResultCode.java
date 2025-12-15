package org.yann.verulia.framework.core.enums;


/**
 *
 * @author Yann
 * @date 2025/12/15 14:03
 */
public interface IResultCode {

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    Integer getCode();

    /**
     * 获取状态码描述
     * @return 状态码描述
     */
    String getMsg();
}
