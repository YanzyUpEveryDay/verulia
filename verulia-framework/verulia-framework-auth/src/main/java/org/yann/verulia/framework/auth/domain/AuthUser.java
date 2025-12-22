package org.yann.verulia.framework.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Yann
 * @date 2025/12/22 15:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements Serializable {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;

    /** 登录类型 (password/miniapp) */
    private String loginType;

    /**
     * 扩展信息
     * 如果业务模块有一些特殊字段(如 avatar, deptId)想带出来，塞到这里面
     */
    private Map<String, Object> extra;
}
