package org.yann.verulia.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yann.verulia.framework.core.domain.PageResult;
import org.yann.verulia.framework.core.domain.R;
import org.yann.verulia.framework.core.enums.UserSourceEnum;
import org.yann.verulia.system.domain.dto.UserDtos;
import org.yann.verulia.system.service.ISysUserService;

/**
 * 用户管理 Controller
 *
 * @author Yann
 */
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final ISysUserService sysUserService;

    /**
     * 分页查询用户列表
     */
    @GetMapping("/page")
    public R<PageResult<UserDtos.Result>> page(UserDtos.Query queryDto) {
        return R.ok(sysUserService.pageQuery(queryDto));
    }

    /**
     * 根据ID获取用户详情
     */
    @GetMapping("/{id}")
    public R<UserDtos.Result> getInfo(@PathVariable Long id) {
        return R.ok(sysUserService.getUserById(id));
    }

    /**
     * 新增用户
     */
    @PostMapping
    public R<Void> add(@RequestBody UserDtos.Create createParams) {
        sysUserService.addUser(createParams);
        return R.ok();
    }

    /**
     * 修改用户
     */
    @PutMapping
    public R<Void> edit(@RequestBody UserDtos.Update updateParams) {
        sysUserService.updateUser(updateParams);
        return R.ok();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        sysUserService.deleteUser(id);
        return R.ok();
    }
}
