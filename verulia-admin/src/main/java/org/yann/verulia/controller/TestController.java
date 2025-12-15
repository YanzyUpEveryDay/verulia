package org.yann.verulia.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yann.verulia.framework.core.domain.R;

/**
 *
 * @author Yann
 * @date 2025/12/15 14:38
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public R<?> test() {
        return R.ok();
    }
}
