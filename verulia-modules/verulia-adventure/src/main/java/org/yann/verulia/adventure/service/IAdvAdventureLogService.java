package org.yann.verulia.adventure.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.yann.verulia.adventure.domain.dto.AdvAdventureLogDtos;
import org.yann.verulia.adventure.domain.entity.AdvAdventureLog;
import org.yann.verulia.framework.core.domain.PageResult;

/**
 * 奇遇手账记录 Service 接口
 *
 * @author Yann
 */
public interface IAdvAdventureLogService extends IService<AdvAdventureLog> {

    /**
     * 分页查询奇遇手账列表
     *
     * @param query 查询参数
     * @return 分页结果
     */
    PageResult<AdvAdventureLogDtos.Result> pageQuery(AdvAdventureLogDtos.Query query);

    /**
     * 根据ID获取奇遇手账详情
     *
     * @param logId 手账ID
     * @return 奇遇手账详情
     */
    AdvAdventureLogDtos.Result getLogById(Long logId);

    /**
     * 修改奇遇手账
     *
     * @param updateParams 修改参数
     */
    void updateLog(AdvAdventureLogDtos.Update updateParams);

    /**
     * 删除奇遇手账
     *
     * @param logId 手账ID
     */
    void deleteLog(Long logId);

    /**
     * 审核奇遇手账
     *
     * @param auditParams 审核参数
     */
    void auditLog(AdvAdventureLogDtos.Audit auditParams);

    /**
     * 获取今日奇遇（APP端）
     * 如果今天没有，则自动生成一个
     *
     * @param userId 用户ID
     * @return 今日奇遇
     */
    AdvAdventureLogDtos.TodayAdventure getTodayAdventure(Long userId);

    /**
     * 换一换（APP端）
     * 更换今日奇遇任务
     *
     * @param userId 用户ID
     * @return 新的奇遇
     */
    AdvAdventureLogDtos.TodayAdventure swapTodayAdventure(Long userId);

    /**
     * 打卡（APP端）
     *
     * @param userId 用户ID
     * @param checkInParams 打卡参数
     */
    void checkIn(Long userId, AdvAdventureLogDtos.CheckIn checkInParams);

    /**
     * 接受奇遇（APP端）
     *
     * @param userId 用户ID
     * @param logId 手账ID
     */
    void acceptAdventure(Long userId, Long logId);

    /**
     * 完成奇遇（APP端）
     *
     * @param userId 用户ID
     * @param logId 手账ID
     */
    void completeAdventure(Long userId, Long logId);
}
