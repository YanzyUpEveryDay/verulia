package org.yann.verulia.framework.auth.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author Yann
 * @date 2025/12/22 17:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginConfig implements Serializable {

    /** 设备类型 (PC/APP) */
    private String device;

    /** 此次登录的 Token 有效期 (单位: 秒)，-1 代表跟随全局 */
    private Long timeout;

    /** 此次登录的 Token 最低活跃频率 (单位: 秒)，-1 代表跟随全局 */
    private Long activeTimeout;

    /** 是否允许并发登录 */
    private Boolean isConcurrent;
}
