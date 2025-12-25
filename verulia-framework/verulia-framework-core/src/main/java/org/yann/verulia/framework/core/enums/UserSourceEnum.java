package org.yann.verulia.framework.core.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Yann
 * @date 2025/12/25 14:01
 */
@Getter
@AllArgsConstructor
public enum UserSourceEnum {

    SYSTEM("system", "后台手动添加"),
    MP_ALIPAY("wx_mp", "微信小程序");

    private final String code;
    private final String info;
}
