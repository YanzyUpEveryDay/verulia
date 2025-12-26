package org.yann.verulia.adventure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yann.verulia.adventure.domain.dto.AdvAdventureLogDtos;
import org.yann.verulia.adventure.service.IAdvAdventureLogService;
import org.yann.verulia.framework.core.domain.PageResult;
import org.yann.verulia.framework.core.domain.R;

/**
 * 奇遇手账记录管理 Controller
 *
 * @author Yann
 */
@RestController
@RequestMapping("/adventure/log")
@RequiredArgsConstructor
public class AdvAdventureLogController {

    private final IAdvAdventureLogService advAdventureLogService;

    /**
     * 分页查询奇遇手账列表
     */
    @GetMapping("/page")
    public R<PageResult<AdvAdventureLogDtos.Result>> page(AdvAdventureLogDtos.Query query) {
        return R.ok(advAdventureLogService.pageQuery(query));
    }

    /**
     * 根据ID获取奇遇手账详情
     */
    @GetMapping("/{logId}")
    public R<AdvAdventureLogDtos.Result> getInfo(@PathVariable Long logId) {
        return R.ok(advAdventureLogService.getLogById(logId));
    }

    /**
     * 修改奇遇手账
     */
    @PutMapping
    public R<Void> edit(@RequestBody AdvAdventureLogDtos.Update updateParams) {
        advAdventureLogService.updateLog(updateParams);
        return R.ok();
    }

    /**
     * 删除奇遇手账
     */
    @DeleteMapping("/{logId}")
    public R<Void> delete(@PathVariable Long logId) {
        advAdventureLogService.deleteLog(logId);
        return R.ok();
    }

    /**
     * 审核奇遇手账
     */
    @PostMapping("/audit")
    public R<Void> audit(@RequestBody AdvAdventureLogDtos.Audit auditParams) {
        advAdventureLogService.auditLog(auditParams);
        return R.ok();
    }
}
