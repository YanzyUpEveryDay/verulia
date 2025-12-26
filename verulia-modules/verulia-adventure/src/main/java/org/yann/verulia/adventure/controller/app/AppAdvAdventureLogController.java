package org.yann.verulia.adventure.controller.app;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yann.verulia.adventure.domain.dto.AdvAdventureLogDtos;
import org.yann.verulia.adventure.service.IAdvAdventureLogService;
import org.yann.verulia.framework.core.domain.R;
import org.yann.verulia.framework.core.service.SecurityContext;

/**
 * APP端 - 奇遇手账 Controller
 *
 * @author Yann
 */
@RestController
@RequestMapping("/app/adventure/log")
@RequiredArgsConstructor
public class AppAdvAdventureLogController {

    private final IAdvAdventureLogService advAdventureLogService;
    private final SecurityContext securityContext;

    /**
     * 获取今日奇遇
     * 如果今天没有，则自动生成一个
     *
     * @return 今日奇遇
     */
    @GetMapping("/today")
    public R<AdvAdventureLogDtos.TodayAdventure> getTodayAdventure() {
        Long userId = securityContext.getUserId();
        return R.ok(advAdventureLogService.getTodayAdventure(userId));
    }

    /**
     * 换一换
     * 更换今日奇遇任务
     *
     * @return 新的奇遇
     */
    @PostMapping("/swap")
    public R<AdvAdventureLogDtos.TodayAdventure> swapTodayAdventure() {
        Long userId = securityContext.getUserId();
        return R.ok(advAdventureLogService.swapTodayAdventure(userId));
    }

    /**
     * 接受奇遇
     *
     * @param logId 手账ID
     * @return 响应结果
     */
    @PostMapping("/accept/{logId}")
    public R<Void> acceptAdventure(@PathVariable Long logId) {
        Long userId = securityContext.getUserId();
        advAdventureLogService.acceptAdventure(userId, logId);
        return R.ok();
    }

    /**
     * 打卡
     *
     * @param checkInParams 打卡参数
     * @return 响应结果
     */
    @PostMapping("/checkin")
    public R<Void> checkIn(@RequestBody AdvAdventureLogDtos.CheckIn checkInParams) {
        Long userId = securityContext.getUserId();
        advAdventureLogService.checkIn(userId, checkInParams);
        return R.ok();
    }

    /**
     * 完成奇遇
     *
     * @param logId 手账ID
     * @return 响应结果
     */
    @PostMapping("/complete/{logId}")
    public R<Void> completeAdventure(@PathVariable Long logId) {
        Long userId = securityContext.getUserId();
        advAdventureLogService.completeAdventure(userId, logId);
        return R.ok();
    }

    /**
     * 获取我的奇遇历史
     *
     * @param query 查询参数
     * @return 奇遇历史列表
     */
    @GetMapping("/history")
    public R<org.yann.verulia.framework.core.domain.PageResult<AdvAdventureLogDtos.Result>> getHistory(AdvAdventureLogDtos.Query query) {
        Long userId = securityContext.getUserId();
        query.setUserId(userId);
        return R.ok(advAdventureLogService.pageQuery(query));
    }

    /**
     * 获取奇遇详情
     *
     * @param logId 手账ID
     * @return 奇遇详情
     */
    @GetMapping("/{logId}")
    public R<AdvAdventureLogDtos.Result> getInfo(@PathVariable Long logId) {
        return R.ok(advAdventureLogService.getLogById(logId));
    }
}
