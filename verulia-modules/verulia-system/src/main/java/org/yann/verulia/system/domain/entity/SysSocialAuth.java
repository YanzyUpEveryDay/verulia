package org.yann.verulia.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 第三方认证信息
 *
 * @author Yann
 */
@Data
@NoArgsConstructor
@TableName("sys_social_auth")
public class SysSocialAuth implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 第三方唯一标识
     */
    private String openid;

    /**
     * 来源 (miniapp, wechat, etc.)
     */
    private String source;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public SysSocialAuth(Long userId, String openid, String source) {
        this.userId = userId;
        this.openid = openid;
        this.source = source;
    }
}
