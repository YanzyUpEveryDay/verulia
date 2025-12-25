package org.yann.verulia.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * C端会员
 *
 * @author Yann
 */
@Data
@TableName("app_member")
public class AppMember implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码(可选，如果是纯验证码登录则不需要)
     */
    private String password;

    /**
     * 微信OpenID
     */
    private String openid;

    /**
     * 微信UnionID
     */
    private String unionid;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 积分
     */
    private Integer score;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 状态(0正常 1封禁)
     */
    private String status;

    /**
     * 注册来源
     */
    private String registerSource;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
