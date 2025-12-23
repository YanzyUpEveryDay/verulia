package org.yann.verulia.system.service.impl;

import cn.hutool.v7.core.bean.BeanUtil;
import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yann.verulia.framework.core.domain.PageResult;
import org.yann.verulia.framework.core.exception.BusinessException;
import org.yann.verulia.system.domain.dto.UserDtos;
import org.yann.verulia.system.domain.entity.SysUser;
import org.yann.verulia.system.domain.entity.SysUserRole;
import org.yann.verulia.system.mapper.SysUserMapper;
import org.yann.verulia.system.mapper.SysUserRoleMapper;
import org.yann.verulia.system.service.ISysUserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户 Service 实现
 *
 * @author Yann
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysUserRoleMapper sysUserRoleMapper;

    @Override
    public PageResult<UserDtos.Result> pageQuery(UserDtos.Query queryDto) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (queryDto.getUsername() != null && !queryDto.getUsername().isBlank()) {
            wrapper.like(SysUser::getUsername, queryDto.getUsername());
        }
        if (queryDto.getStatus() != null) {
            wrapper.eq(SysUser::getStatus, queryDto.getStatus());
        }
        if (queryDto.getPhone() != null && !queryDto.getPhone().isBlank()) {
            wrapper.eq(SysUser::getPhone, queryDto.getPhone());
        }

        wrapper.orderByDesc(SysUser::getCreateTime);

        Page<SysUser> page = new Page<>(queryDto.getPageNum(), queryDto.getPageSize());
        Page<SysUser> userPage = this.page(page, wrapper);

        List<UserDtos.Result> voList = userPage.getRecords().stream()
            .map(this::toResult)
            .collect(Collectors.toList());

        return PageResult.of(voList, userPage.getTotal());
    }

    @Override
    public UserDtos.Result getUserById(Long id) {
        SysUser user = this.getById(id);
        if (user == null) {
            return null;
        }
        
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        
        return UserDtos.Result.fromSysUser(user, roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserDtos.Create createParams) {
        if (exists(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, createParams.username()))) {
            throw new BusinessException("用户名已存在");
        }

        SysUser user = new SysUser();
        BeanUtil.copyProperties(createParams, user);

        user.setPassword(SecureUtil.sha256(createParams.password()));

        if (user.getStatus() == null) {
            user.setStatus(1);
        }

        this.save(user);
        insertUserRole(user.getId(), createParams.roleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserDtos.Update updateParams) {
        SysUser user = this.getById(updateParams.id());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (updateParams.nickname() != null) user.setNickname(updateParams.nickname());
        if (updateParams.email() != null) user.setEmail(updateParams.email());
        if (updateParams.phone() != null) user.setPhone(updateParams.phone());
        if (updateParams.sex() != null) user.setSex(updateParams.sex());
        if (updateParams.status() != null) user.setStatus(updateParams.status());

        this.updateById(user);

        // Update user roles
        List<Long> roleIds = updateParams.roleIds();
        if (roleIds != null) {
            // Delete existing
            sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, user.getId()));
            // Insert new
            insertUserRole(user.getId(), roleIds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        
        if (!this.removeById(id)) {
            throw new BusinessException("删除失败，用户不存在");
        }
    }

    private void insertUserRole(Long userId, List<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            for (Long roleId : roleIds) {
                sysUserRoleMapper.insert(new SysUserRole(userId, roleId));
            }
        }
    }

    private UserDtos.Result toResult(SysUser user) {
        return UserDtos.Result.fromSysUser(user);
    }
}
