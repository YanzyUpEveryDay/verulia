package org.yann.verulia.adventure.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.yann.verulia.framework.core.domain.BasePageQuery;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 奇遇手账记录数据传输对象聚合
 *
 * @author Yann
 */
public interface AdvAdventureLogDtos {

    /**
     * 奇遇手账查询参数
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    class Query extends BasePageQuery {
        /**
         * 冒险家ID（用户ID）
         */
        private Long userId;

        /**
         * 关联奇遇ID
         */
        private Long taskId;

        /**
         * 奇遇日期-开始
         */
        private LocalDate startDate;

        /**
         * 奇遇日期-结束
         */
        private LocalDate endDate;

        /**
         * 状态（0待接受 1进行中 2已完成）
         */
        private String status;

        /**
         * 审核状态（0未审核 1已审核）
         */
        private String auditStatus;
    }

    /**
     * 修改奇遇手账参数
     */
    record Update(
        Long logId,
        String checkinImg,
        String checkinComment,
        String aiReply,
        String status,
        String auditStatus
    ) implements Serializable {}

    /**
     * 审核参数
     */
    record Audit(
        Long logId,
        String auditStatus,
        String aiReply
    ) implements Serializable {}

    /**
     * 奇遇手账结果视图
     */
    record Result(
        Long logId,
        Long userId,
        Long taskId,
        String taskTitle,
        LocalDate adventureDate,
        String status,
        Integer swapCount,
        String checkinImg,
        String checkinComment,
        String aiReply,
        String auditStatus,
        LocalDateTime finishTime,
        String createBy,
        LocalDateTime createTime,
        String updateBy,
        LocalDateTime updateTime
    ) implements Serializable {}

    /**
     * APP端-今日奇遇
     */
    record TodayAdventure(
        Long logId,
        Long taskId,
        String taskTitle,
        String taskContent,
        String category,
        Integer difficulty,
        LocalDate adventureDate,
        String status,
        Integer swapCount,
        String checkinImg,
        String checkinComment,
        String aiReply,
        LocalDateTime finishTime
    ) implements Serializable {}

    /**
     * APP端-打卡参数
     */
    record CheckIn(
        Long logId,
        String checkinImg,
        String checkinComment
    ) implements Serializable {}
}
