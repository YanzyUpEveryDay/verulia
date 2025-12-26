package org.yann.verulia.adventure.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.yann.verulia.framework.core.domain.BaseEntity;

/**
 * 奇遇任务实体
 *
 * @author Yann
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("adv_task")
public class AdvTask extends BaseEntity {

    /**
     * 奇遇ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 奇遇标题
     */
    private String title;

    /**
     * 奇遇内容
     */
    private String content;

    /**
     * 奇遇分类
     */
    private String category;

    /**
     * 难度等级
     */
    private Integer difficulty;

    /**
     * 状态（1正常 0停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String deleted;

    /**
     * 备注
     */
    private String remark;
}
