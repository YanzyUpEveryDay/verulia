package org.yann.verulia.adventure.service.impl;

import cn.hutool.v7.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yann.verulia.adventure.domain.dto.AdvAdventureLogDtos;
import org.yann.verulia.adventure.domain.entity.AdvAdventureLog;
import org.yann.verulia.adventure.domain.entity.AdvTask;
import org.yann.verulia.adventure.mapper.AdvAdventureLogMapper;
import org.yann.verulia.adventure.mapper.AdvTaskMapper;
import org.yann.verulia.adventure.service.IAdvAdventureLogService;
import org.yann.verulia.framework.core.domain.PageResult;
import org.yann.verulia.framework.core.exception.BusinessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 奇遇手账记录 Service 实现
 *
 * @author Yann
 */
@Service
@RequiredArgsConstructor
public class AdvAdventureLogServiceImpl extends ServiceImpl<AdvAdventureLogMapper, AdvAdventureLog> implements IAdvAdventureLogService {

    private final AdvTaskMapper advTaskMapper;

    @Override
    public PageResult<AdvAdventureLogDtos.Result> pageQuery(AdvAdventureLogDtos.Query query) {
        LambdaQueryWrapper<AdvAdventureLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getUserId() != null, AdvAdventureLog::getUserId, query.getUserId())
               .eq(query.getTaskId() != null, AdvAdventureLog::getTaskId, query.getTaskId())
               .ge(query.getStartDate() != null, AdvAdventureLog::getAdventureDate, query.getStartDate())
               .le(query.getEndDate() != null, AdvAdventureLog::getAdventureDate, query.getEndDate())
               .eq(query.getStatus() != null, AdvAdventureLog::getStatus, query.getStatus())
               .eq(query.getAuditStatus() != null, AdvAdventureLog::getAuditStatus, query.getAuditStatus())
               .orderByDesc(AdvAdventureLog::getCreateTime);

        Page<AdvAdventureLog> page = new Page<>(query.getPageNum(), query.getPageSize());
        Page<AdvAdventureLog> logPage = this.page(page, wrapper);
        
        List<AdvAdventureLogDtos.Result> resultList = logPage.getRecords().stream()
            .map(this::convertToResult)
            .collect(Collectors.toList());

        return PageResult.of(resultList, logPage.getTotal());
    }

    @Override
    public AdvAdventureLogDtos.Result getLogById(Long logId) {
        AdvAdventureLog log = this.getById(logId);
        if (log == null) {
            throw new BusinessException("奇遇手账不存在");
        }
        return convertToResult(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLog(AdvAdventureLogDtos.Update updateParams) {
        AdvAdventureLog log = this.getById(updateParams.logId());
        if (log == null) {
            throw new BusinessException("奇遇手账不存在");
        }
        BeanUtil.copyProperties(updateParams, log);
        this.updateById(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLog(Long logId) {
        AdvAdventureLog log = this.getById(logId);
        if (log == null) {
            throw new BusinessException("奇遇手账不存在");
        }
        this.removeById(logId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditLog(AdvAdventureLogDtos.Audit auditParams) {
        AdvAdventureLog log = this.getById(auditParams.logId());
        if (log == null) {
            throw new BusinessException("奇遇手账不存在");
        }
        log.setAuditStatus(auditParams.auditStatus());
        if (auditParams.aiReply() != null) {
            log.setAiReply(auditParams.aiReply());
        }
        this.updateById(log);
    }

    @Override
    public AdvAdventureLogDtos.TodayAdventure getTodayAdventure(Long userId) {
        LocalDate today = LocalDate.now();
        
        // 查询今日是否已有奇遇
        AdvAdventureLog log = baseMapper.selectByUserAndDate(userId, today);
        
        if (log == null) {
            // 自动生成今日奇遇
            log = createTodayAdventure(userId, today);
        }
        
        return convertToTodayAdventure(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdvAdventureLogDtos.TodayAdventure swapTodayAdventure(Long userId) {
        LocalDate today = LocalDate.now();
        
        AdvAdventureLog log = baseMapper.selectByUserAndDate(userId, today);
        if (log == null) {
            throw new BusinessException("今日还没有奇遇，无法换一换");
        }
        
        // 只有待接受状态才能换一换
        if (!"0".equals(log.getStatus())) {
            throw new BusinessException("只有待接受状态的奇遇才能换一换");
        }
        
        // 获取新的随机任务
        AdvTask newTask = advTaskMapper.selectRandomTask(null);
        if (newTask == null) {
            throw new BusinessException("暂无可用的奇遇任务");
        }
        
        // 更新任务ID和换一换次数
        log.setTaskId(newTask.getId());
        log.setSwapCount(log.getSwapCount() + 1);
        this.updateById(log);
        
        return convertToTodayAdventure(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkIn(Long userId, AdvAdventureLogDtos.CheckIn checkInParams) {
        AdvAdventureLog log = this.getById(checkInParams.logId());
        if (log == null) {
            throw new BusinessException("奇遇手账不存在");
        }
        
        if (!log.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此奇遇");
        }
        
        if (!"1".equals(log.getStatus())) {
            throw new BusinessException("只有进行中的奇遇才能打卡");
        }
        
        log.setCheckinImg(checkInParams.checkinImg());
        log.setCheckinComment(checkInParams.checkinComment());
        // 打卡后自动完成
        log.setStatus("2");
        log.setFinishTime(LocalDateTime.now());
        this.updateById(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptAdventure(Long userId, Long logId) {
        AdvAdventureLog log = this.getById(logId);
        if (log == null) {
            throw new BusinessException("奇遇手账不存在");
        }
        
        if (!log.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此奇遇");
        }
        
        if (!"0".equals(log.getStatus())) {
            throw new BusinessException("只有待接受状态的奇遇才能接受");
        }
        
        log.setStatus("1");
        this.updateById(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeAdventure(Long userId, Long logId) {
        AdvAdventureLog log = this.getById(logId);
        if (log == null) {
            throw new BusinessException("奇遇手账不存在");
        }
        
        if (!log.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此奇遇");
        }
        
        if (!"1".equals(log.getStatus())) {
            throw new BusinessException("只有进行中的奇遇才能完成");
        }
        
        log.setStatus("2");
        log.setFinishTime(LocalDateTime.now());
        this.updateById(log);
    }

    /**
     * 创建今日奇遇
     */
    private AdvAdventureLog createTodayAdventure(Long userId, LocalDate today) {
        // 获取随机任务
        AdvTask randomTask = advTaskMapper.selectRandomTask(null);
        if (randomTask == null) {
            throw new BusinessException("暂无可用的奇遇任务");
        }
        
        AdvAdventureLog log = new AdvAdventureLog();
        log.setUserId(userId);
        log.setTaskId(randomTask.getId());
        log.setAdventureDate(today);
        log.setStatus("0"); // 待接受
        log.setSwapCount(0);
        log.setAuditStatus("0"); // 未审核
        this.save(log);
        
        return log;
    }

    /**
     * 转换为结果视图
     */
    private AdvAdventureLogDtos.Result convertToResult(AdvAdventureLog log) {
        // 查询任务标题
        AdvTask task = advTaskMapper.selectById(log.getTaskId());
        String taskTitle = task != null ? task.getTitle() : null;
        
        return new AdvAdventureLogDtos.Result(
            log.getLogId(),
            log.getUserId(),
            log.getTaskId(),
            taskTitle,
            log.getAdventureDate(),
            log.getStatus(),
            log.getSwapCount(),
            log.getCheckinImg(),
            log.getCheckinComment(),
            log.getAiReply(),
            log.getAuditStatus(),
            log.getFinishTime(),
            log.getCreateBy(),
            log.getCreateTime(),
            log.getUpdateBy(),
            log.getUpdateTime()
        );
    }

    /**
     * 转换为今日奇遇视图
     */
    private AdvAdventureLogDtos.TodayAdventure convertToTodayAdventure(AdvAdventureLog log) {
        AdvTask task = advTaskMapper.selectById(log.getTaskId());
        if (task == null) {
            throw new BusinessException("关联的奇遇任务不存在");
        }
        
        return new AdvAdventureLogDtos.TodayAdventure(
            log.getLogId(),
            log.getTaskId(),
            task.getTitle(),
            task.getContent(),
            task.getCategory(),
            task.getDifficulty(),
            log.getAdventureDate(),
            log.getStatus(),
            log.getSwapCount(),
            log.getCheckinImg(),
            log.getCheckinComment(),
            log.getAiReply(),
            log.getFinishTime()
        );
    }
}
