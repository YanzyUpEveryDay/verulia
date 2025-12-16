package org.yann.verulia.system.domain.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.yann.verulia.framework.core.domain.BasePageQuery;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色数据传输对象聚合
 *
 * @author Yann
 */
public interface RoleDtos {

    /**
     * 角色查询参数
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    class Query extends BasePageQuery {
        /**
         * 角色名称
         */
        private String roleName;

        /**
         * 权限字符
         */
        private String roleKey;

        /**
         * 状态
         */
        private Integer status;
        
        public <T> Page<T> toPage() {
            return new Page<>(this.getPageNum(), this.getPageSize());
        }
    }

    /**
     * 新增角色参数
     */
    record Create(
        String roleName,
        String roleKey,
        Integer roleSort,
        Integer status
    ) implements Serializable {}

    /**
     * 修改角色参数
     */
    record Update(
        Long id,
        String roleName,
        String roleKey,
        Integer roleSort,
        Integer status
    ) implements Serializable {}

    /**
     * 角色结果视图
     */
    record Result(
        Long id,
        String roleName,
        String roleKey,
        Integer roleSort,
        Integer status,
        LocalDateTime createTime
    ) implements Serializable {}
}
