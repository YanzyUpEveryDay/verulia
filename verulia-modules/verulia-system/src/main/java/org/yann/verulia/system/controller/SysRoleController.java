package org.yann.verulia.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yann.verulia.framework.core.domain.PageResult;
import org.yann.verulia.framework.core.domain.R;
import org.yann.verulia.system.domain.dto.RoleDtos;
import org.yann.verulia.system.service.ISysRoleService;

/**
 * 角色管理 Controller
 *
 * @author Yann
 */
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final ISysRoleService sysRoleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping("/page")
    public R<PageResult<RoleDtos.Result>> page(RoleDtos.Query query) {
        return R.ok(sysRoleService.pageQuery(query));
    }

    /**
     * 根据ID获取角色详情
     */
    @GetMapping("/{id}")
    public R<RoleDtos.Result> getInfo(@PathVariable Long id) {
        return R.ok(sysRoleService.getRoleById(id));
    }

    /**
     * 新增角色
     */
    @PostMapping
    public R<Void> add(@RequestBody RoleDtos.Create createParams) {
        sysRoleService.addRole(createParams);
        return R.ok();
    }

    /**
     * 修改角色
     */
    @PutMapping
    public R<Void> edit(@RequestBody RoleDtos.Update updateParams) {
        sysRoleService.updateRole(updateParams);
        return R.ok();
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        sysRoleService.deleteRole(id);
        return R.ok();
    }
}
