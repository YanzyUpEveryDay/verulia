package org.yann.verulia.system.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.yann.verulia.framework.core.domain.BasePageQuery;
import org.yann.verulia.system.domain.entity.SysUser;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 用户数据传输对象聚合
 *
 * @author Yann
 */
public interface UserDtos {

    @Data
    @EqualsAndHashCode(callSuper = true)
    class Query extends BasePageQuery {
        private String username;
        private String phone;
        private Integer status;
    }

    /**
     * 新增用户参数
     */
    record Create(
        String username,
        String password,
        String nickname,
        String email,
        String phone,
        Integer sex,
        Integer status,
        List<Long> roleIds
    ) implements Serializable {}

    /**
     * 修改用户参数
     */
    record Update(
        Long id,
        String nickname,
        String email,
        String phone,
        Integer sex,
        Integer status,
        List<Long> roleIds
    ) implements Serializable {}

    /**
     * 用户结果视图 (VO)
     */
    record Result(
        Long id,
        String username,
        String nickname,
        String email,
        String phone,
        Integer sex,
        Integer status,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        List<Long> roleIds
    ) implements Serializable {
        /**
         * 从 SysUser 转换为 Result
         */
        public static Result fromSysUser(SysUser user) {
            return fromSysUser(user, Collections.emptyList());
        }

        /**
         * 从 SysUser 转换为 Result (带角色ID)
         */
        public static Result fromSysUser(SysUser user, List<Long> roleIds) {
            if (user == null) {
                return null;
            }
            return new Result(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getPhone(),
                user.getSex(),
                user.getStatus(),
                user.getCreateTime(),
                user.getUpdateTime(),
                roleIds
            );
        }
    }
}
