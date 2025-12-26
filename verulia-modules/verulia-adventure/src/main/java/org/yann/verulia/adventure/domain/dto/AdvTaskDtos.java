package org.yann.verulia.adventure.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.yann.verulia.framework.core.domain.BasePageQuery;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 奇遇任务数据传输对象聚合
 *
 * @author Yann
 */
public interface AdvTaskDtos {

    /**
     * 奇遇任务查询参数
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    class Query extends BasePageQuery {
        /**
         * 奇遇标题
         */
        private String title;

        /**
         * 奇遇分类
         */
        private String category;

        /**
         * 难度等级
         */
        private Integer difficulty;

        /**
         * 状态
         */
        private String status;
    }

    /**
     * 新增奇遇任务参数
     */
    record Create(
        String title,
        String content,
        String category,
        Integer difficulty,
        String status,
        String remark
    ) implements Serializable {}

    /**
     * 修改奇遇任务参数
     */
    record Update(
        Long id,
        String title,
        String content,
        String category,
        Integer difficulty,
        String status,
        String remark
    ) implements Serializable {}

    /**
     * 奇遇任务结果视图
     */
    record Result(
        Long id,
        String title,
        String content,
        String category,
        Integer difficulty,
        String status,
        String remark,
        String createBy,
        LocalDateTime createTime,
        String updateBy,
        LocalDateTime updateTime
    ) implements Serializable {}
}
