package org.yann.verulia.adventure.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.yann.verulia.adventure.domain.dto.AdvTaskDtos;
import org.yann.verulia.adventure.domain.entity.AdvTask;
import org.yann.verulia.framework.core.domain.PageResult;

/**
 * 奇遇任务 Service 接口
 *
 * @author Yann
 */
public interface IAdvTaskService extends IService<AdvTask> {

    /**
     * 分页查询奇遇任务列表
     *
     * @param query 查询参数
     * @return 分页结果
     */
    PageResult<AdvTaskDtos.Result> pageQuery(AdvTaskDtos.Query query);

    /**
     * 根据ID获取奇遇任务详情
     *
     * @param id 奇遇任务ID
     * @return 奇遇任务详情
     */
    AdvTaskDtos.Result getTaskById(Long id);

    /**
     * 新增奇遇任务
     *
     * @param createParams 新增参数
     */
    void addTask(AdvTaskDtos.Create createParams);

    /**
     * 修改奇遇任务
     *
     * @param updateParams 修改参数
     */
    void updateTask(AdvTaskDtos.Update updateParams);

    /**
     * 删除奇遇任务
     *
     * @param id 奇遇任务ID
     */
    void deleteTask(Long id);

    /**
     * 获取随机奇遇任务
     *
     * @param category 奇遇类型（可选）
     * @return 随机奇遇任务
     */
    AdvTaskDtos.Result getRandomTask(String category);
}
