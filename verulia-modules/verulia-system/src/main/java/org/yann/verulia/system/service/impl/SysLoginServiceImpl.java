package org.yann.verulia.system.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yann.verulia.framework.core.exception.BusinessException;
import org.yann.verulia.system.domain.entity.SysUser;
import org.yann.verulia.system.domain.vo.LoginVo;
import org.yann.verulia.system.mapper.SysUserMapper;
import org.yann.verulia.system.service.ISysLoginService;
import org.yann.verulia.system.service.ISysRoleService;

import java.util.Set;

/**
 * 登录 Service 实现
 *
 * @author Yann
 */
@Service
@RequiredArgsConstructor
public class SysLoginServiceImpl implements ISysLoginService {

    private final SysUserMapper sysUserMapper;
    private final ISysRoleService roleService;

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
        /* 获取用户角色 */
        Set<String> userRoleKeys = roleService.getUserRoleKeys(user.getId());
        /* #TODO 预留用户全权限 */

        return LoginVo.builder()
            .user(LoginVo.UserInfoVo.fromSysUser(user))
            .roles(userRoleKeys)
            .build();
    }
}
