package org.yann.verulia.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.yann.verulia.framework.core.exception.BusinessException;
import org.yann.verulia.system.domain.dto.UserDtos;
import org.yann.verulia.system.domain.entity.SysRole;
import org.yann.verulia.system.domain.entity.SysUser;
import org.yann.verulia.system.domain.entity.SysUserRole;
import org.yann.verulia.system.domain.vo.LoginVo;
import org.yann.verulia.system.mapper.SysRoleMapper;
import org.yann.verulia.system.mapper.SysUserMapper;
import org.yann.verulia.system.mapper.SysUserRoleMapper;
import org.yann.verulia.system.service.ISysLoginService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 登录 Service 实现
 *
 * @author Yann
 */
@Service
@RequiredArgsConstructor
public class SysLoginServiceImpl implements ISysLoginService {

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public LoginVo getRemoteInfo(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return buildLoginVo(user);
    }


    /**
     * 构建登录响应
     */
    private LoginVo buildLoginVo(SysUser user) {
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
            .eq(SysUserRole::getUserId, user.getId()));
        
        List<String> roleKeys = Collections.emptyList();
        if (!userRoles.isEmpty()) {
            List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            List<SysRole> roles = sysRoleMapper.selectByIds(roleIds);
            roleKeys = roles.stream()
                .map(SysRole::getRoleKey)
                .collect(Collectors.toList());
        }

        // 转换用户响应
        UserDtos.Result userInfo = UserDtos.Result.fromSysUser(user);
        return new LoginVo(
            StpUtil.getTokenValue(),
            userInfo,
            roleKeys
        );
    }
}
