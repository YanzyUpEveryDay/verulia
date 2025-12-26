package org.yann.verulia.adventure.service.impl;

import cn.hutool.v7.core.bean.BeanUtil;
import cn.hutool.v7.core.text.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yann.verulia.adventure.domain.dto.AdvTaskDtos;
import org.yann.verulia.adventure.domain.entity.AdvTask;
import org.yann.verulia.adventure.mapper.AdvTaskMapper;
import org.yann.verulia.adventure.service.IAdvTaskService;
import org.yann.verulia.framework.core.domain.PageResult;
import org.yann.verulia.framework.core.exception.BusinessException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 奇遇任务 Service 实现
 *
 * @author Yann
 */
@Service
@RequiredArgsConstructor
public class AdvTaskServiceImpl extends ServiceImpl<AdvTaskMapper, AdvTask> implements IAdvTaskService {

    @Override
    public PageResult<AdvTaskDtos.Result> pageQuery(AdvTaskDtos.Query query) {
        LambdaQueryWrapper<AdvTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(query.getTitle()), AdvTask::getTitle, query.getTitle())
               .eq(StrUtil.isNotBlank(query.getCategory()), AdvTask::getCategory, query.getCategory())
               .eq(query.getDifficulty() != null, AdvTask::getDifficulty, query.getDifficulty())
               .eq(StrUtil.isNotBlank(query.getStatus()), AdvTask::getStatus, query.getStatus())
               .orderByDesc(AdvTask::getCreateTime);

        Page<AdvTask> page = new Page<>(query.getPageNum(), query.getPageSize());
        Page<AdvTask> taskPage = this.page(page, wrapper);
        List<AdvTaskDtos.Result> resultList = taskPage.getRecords().stream()
            .map(this::convertToResult)
            .collect(Collectors.toList());

        return PageResult.of(resultList, taskPage.getTotal());
    }

    @Override
    public AdvTaskDtos.Result getTaskById(Long id) {
        AdvTask task = this.getById(id);
        if (task == null) {
            throw new BusinessException("奇遇任务不存在");
        }
        return convertToResult(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTask(AdvTaskDtos.Create createParams) {
        AdvTask task = new AdvTask();
        BeanUtil.copyProperties(createParams, task);
        this.save(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTask(AdvTaskDtos.Update updateParams) {
        AdvTask task = this.getById(updateParams.id());
        if (task == null) {
            throw new BusinessException("奇遇任务不存在");
        }
        BeanUtil.copyProperties(updateParams, task);
        this.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(Long id) {
        AdvTask task = this.getById(id);
        if (task == null) {
            throw new BusinessException("奇遇任务不存在");
        }
        this.removeById(id);
    }

    @Override
    public AdvTaskDtos.Result getRandomTask(String category) {
        // 使用数据库层面的随机查询，避免全量加载到内存
        AdvTask randomTask = baseMapper.selectRandomTask(category);
        if (randomTask == null) {
            throw new BusinessException("暂无可用的奇遇任务");
        }
        return convertToResult(randomTask);
    }

    /**
     * 转换为结果视图
     */
    private AdvTaskDtos.Result convertToResult(AdvTask task) {
        return new AdvTaskDtos.Result(
            task.getId(),
            task.getTitle(),
            task.getContent(),
            task.getCategory(),
            task.getDifficulty(),
            task.getStatus(),
            task.getRemark(),
            task.getCreateBy(),
            task.getCreateTime(),
            task.getUpdateBy(),
            task.getUpdateTime()
        );
    }
}
