package org.yann.verulia.system.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.yann.verulia.framework.core.domain.BasePageQuery;

import java.io.Serializable;
import java.time.LocalDateTime;

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
        Integer status
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
        Integer status
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
        LocalDateTime updateTime
    ) implements Serializable {}
}
