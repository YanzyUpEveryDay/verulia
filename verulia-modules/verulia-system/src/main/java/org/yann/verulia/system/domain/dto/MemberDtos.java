package org.yann.verulia.system.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.yann.verulia.framework.core.domain.BasePageQuery;
import org.yann.verulia.system.domain.entity.AppMember;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员数据传输对象聚合
 *
 * @author Yann
 */
public interface MemberDtos {

    @Data
    @EqualsAndHashCode(callSuper = true)
    class Query extends BasePageQuery {
        private String nickname;
        private String mobile;
        private Integer level;
        private String status;
    }

    /**
     * 新增会员参数
     */
    record Create(
        String nickname,
        String mobile,
        String password,
        String avatar,
        Integer level,
        String status,
        String registerSource
    ) implements Serializable {}

    /**
     * 微信注册会员参数
     */
    record WechatRegister(
        String openid,
        String unionid,
        String nickname,
        String avatar,
        String registerSource
    ) implements Serializable {}

    /**
     * 修改会员参数
     */
    record Update(
        Long id,
        String nickname,
        String mobile,
        String avatar,
        Integer level,
        String status
    ) implements Serializable {}

    /**
     * 会员结果视图 (VO)
     */
    record Result(
        Long id,
        String nickname,
        String avatar,
        String mobile,
        String openid,
        String unionid,
        BigDecimal balance,
        Integer score,
        Integer level,
        String status,
        String registerSource,
        LocalDateTime createTime
    ) implements Serializable {
        /**
         * 从 AppMember 转换为 Result
         */
        public static Result fromAppMember(AppMember member) {
            if (member == null) {
                return null;
            }
            return new Result(
                member.getId(),
                member.getNickname(),
                member.getAvatar(),
                member.getMobile(),
                member.getOpenid(),
                member.getUnionid(),
                member.getBalance(),
                member.getScore(),
                member.getLevel(),
                member.getStatus(),
                member.getRegisterSource(),
                member.getCreateTime()
            );
        }
    }
}
