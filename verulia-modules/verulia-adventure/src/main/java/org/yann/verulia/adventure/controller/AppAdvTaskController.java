package org.yann.verulia.adventure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yann.verulia.adventure.domain.dto.AdvTaskDtos;
import org.yann.verulia.adventure.service.IAdvTaskService;
import org.yann.verulia.framework.core.domain.R;

/**
 * APP端 - 奇遇任务 Controller
 *
 * @author Yann
 */
@RestController
@RequestMapping("/app/adventure/task")
@RequiredArgsConstructor
public class AppAdvTaskController {

    private final IAdvTaskService advTaskService;

    /**
     * 获取随机奇遇任务
     * 可以通过奇遇类型进行查询获取随机任务
     *
     * @param category 奇遇类型（可选）
     * @return 随机奇遇任务
     */
    @GetMapping("/random")
    public R<AdvTaskDtos.Result> getRandomTask(@RequestParam(name = "category", required = false) String category) {
        return R.ok(advTaskService.getRandomTask(category));
    }
}
