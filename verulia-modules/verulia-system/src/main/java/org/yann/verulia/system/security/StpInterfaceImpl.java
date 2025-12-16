package org.yann.verulia.system.security;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yann.verulia.system.domain.entity.SysRole;
import org.yann.verulia.system.domain.entity.SysUserRole;
import org.yann.verulia.system.mapper.SysRoleMapper;
import org.yann.verulia.system.mapper.SysUserRoleMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sa-Token 权限加载接口实现
 *
 * @author Yann
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 暂不实现权限
        return Collections.emptyList();
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId;
        try {
            userId = Long.parseLong(loginId.toString());
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }

        // 1. 查询用户关联的所有角色ID
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
            new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId)
        );

        if (userRoles.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> roleIds = userRoles.stream()
            .map(SysUserRole::getRoleId)
            .collect(Collectors.toList());

        // 2. 查询对应的角色权限字符
        List<SysRole> roles = sysRoleMapper.selectByIds(roleIds);
        
        return roles.stream()
            .map(SysRole::getRoleKey)
            .filter(key -> key != null && !key.isBlank())
            .collect(Collectors.toList());
    }
}
