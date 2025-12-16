package org.yann.verulia.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.yann.verulia.framework.core.domain.PageResult;
import org.yann.verulia.system.domain.dto.RoleDtos;
import org.yann.verulia.system.domain.entity.SysRole;

/**
 * 角色 Service 接口
 *
 * @author Yann
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     *
     * @param query 查询参数
     * @return 分页结果
     */
    PageResult<RoleDtos.Result> pageQuery(RoleDtos.Query query);

    /**
     * 根据ID获取角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    RoleDtos.Result getRoleById(Long id);

    /**
     * 新增角色
     *
     * @param createParams 新增参数
     */
    void addRole(RoleDtos.Create createParams);

    /**
     * 修改角色
     *
     * @param updateParams 修改参数
     */
    void updateRole(RoleDtos.Update updateParams);

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    void deleteRole(Long id);
}
