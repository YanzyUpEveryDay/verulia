package org.yann.verulia.adventure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yann.verulia.adventure.domain.dto.AdvTaskDtos;
import org.yann.verulia.adventure.service.IAdvTaskService;
import org.yann.verulia.framework.core.domain.PageResult;
import org.yann.verulia.framework.core.domain.R;

/**
 * 奇遇任务管理 Controller
 *
 * @author Yann
 */
@RestController
@RequestMapping("/adventure/task")
@RequiredArgsConstructor
public class AdvTaskController {

    private final IAdvTaskService advTaskService;

    /**
     * 分页查询奇遇任务列表
     */
    @GetMapping("/page")
    public R<PageResult<AdvTaskDtos.Result>> page(AdvTaskDtos.Query query) {
        return R.ok(advTaskService.pageQuery(query));
    }

    /**
     * 根据ID获取奇遇任务详情
     */
    @GetMapping("/{id}")
    public R<AdvTaskDtos.Result> getInfo(@PathVariable Long id) {
        return R.ok(advTaskService.getTaskById(id));
    }

    /**
     * 新增奇遇任务
     */
    @PostMapping
    public R<Void> add(@RequestBody AdvTaskDtos.Create createParams) {
        advTaskService.addTask(createParams);
        return R.ok();
    }

    /**
     * 修改奇遇任务
     */
    @PutMapping
    public R<Void> edit(@RequestBody AdvTaskDtos.Update updateParams) {
        advTaskService.updateTask(updateParams);
        return R.ok();
    }

    /**
     * 删除奇遇任务
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        advTaskService.deleteTask(id);
        return R.ok();
    }
}
