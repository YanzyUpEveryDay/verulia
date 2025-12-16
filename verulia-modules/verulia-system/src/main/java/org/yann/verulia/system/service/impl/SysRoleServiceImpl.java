package org.yann.verulia.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yann.verulia.framework.core.domain.PageResult;
import org.yann.verulia.framework.core.exception.BusinessException;
import org.yann.verulia.system.domain.dto.RoleDtos;
import org.yann.verulia.system.domain.entity.SysRole;
import org.yann.verulia.system.domain.entity.SysUserRole;
import org.yann.verulia.system.mapper.SysRoleMapper;
import org.yann.verulia.system.mapper.SysUserRoleMapper;
import org.yann.verulia.system.service.ISysRoleService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色 Service 实现
 *
 * @author Yann
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final SysUserRoleMapper sysUserRoleMapper;

    @Override
    public PageResult<RoleDtos.Result> pageQuery(RoleDtos.Query query) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (query.getRoleName() != null && !query.getRoleName().isBlank()) {
            wrapper.like(SysRole::getRoleName, query.getRoleName());
        }
        if (query.getRoleKey() != null && !query.getRoleKey().isBlank()) {
            wrapper.eq(SysRole::getRoleKey, query.getRoleKey());
        }
        if (query.getStatus() != null) {
            wrapper.eq(SysRole::getStatus, query.getStatus());
        }
        
        wrapper.orderByAsc(SysRole::getRoleSort).orderByDesc(SysRole::getCreateTime);

        Page<SysRole> page = query.toPage();
        Page<SysRole> rolePage = this.page(page, wrapper);
        
        List<RoleDtos.Result> voList = rolePage.getRecords().stream()
                .map(role -> BeanUtil.copyProperties(role, RoleDtos.Result.class))
                .collect(Collectors.toList());
                
        return PageResult.of(voList, rolePage.getTotal());
    }

    @Override
    public RoleDtos.Result getRoleById(Long id) {
        SysRole role = this.getById(id);
        if (role == null) {
            return null;
        }
        return BeanUtil.copyProperties(role, RoleDtos.Result.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(RoleDtos.Create createParams) {
        checkRoleNameUnique(createParams.roleName());
        checkRoleKeyUnique(createParams.roleKey());

        SysRole role = new SysRole();
        BeanUtil.copyProperties(createParams, role);
        this.save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleDtos.Update updateParams) {
        SysRole role = this.getById(updateParams.id());
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        if (!role.getRoleName().equals(updateParams.roleName())) {
            checkRoleNameUnique(updateParams.roleName());
        }
        if (!role.getRoleKey().equals(updateParams.roleKey())) {
            checkRoleKeyUnique(updateParams.roleKey());
        }

        BeanUtil.copyProperties(updateParams, role);
        this.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        long count = sysUserRoleMapper.selectCount(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, id));
        if (count > 0) {
            throw new BusinessException("该角色已分配用户，无法删除");
        }
        
        if (!this.removeById(id)) {
            throw new BusinessException("删除失败，角色不存在");
        }
    }

    private void checkRoleNameUnique(String roleName) {
        if (this.exists(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleName, roleName))) {
            throw new BusinessException("角色名称已存在");
        }
    }

    private void checkRoleKeyUnique(String roleKey) {
        if (this.exists(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey, roleKey))) {
            throw new BusinessException("权限字符已存在");
        }
    }
}
