package org.yann.verulia.adventure.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.yann.verulia.framework.core.domain.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 奇遇手账记录实体
 *
 * @author Yann
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("adv_adventure_log")
public class AdvAdventureLog extends BaseEntity {

    /**
     * 手账ID
     */
    @TableId(type = IdType.AUTO)
    private Long logId;

    /**
     * 冒险家ID（用户ID）
     */
    private Long userId;

    /**
     * 关联奇遇ID
     */
    private Long taskId;

    /**
     * 奇遇日期
     */
    private LocalDate adventureDate;

    /**
     * 状态（0待接受 1进行中 2已完成）
     */
    private String status;

    /**
     * 换一换次数
     */
    private Integer swapCount;

    /**
     * 打卡照片
     */
    private String checkinImg;

    /**
     * 打卡心得
     */
    private String checkinComment;

    /**
     * AI回信
     */
    private String aiReply;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String deleted;

    /**
     * 审核状态（0未审核 1已审核）
     */
    private String auditStatus;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;
}
