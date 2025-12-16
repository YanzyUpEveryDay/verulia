package org.yann.verulia.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.yann.verulia.framework.core.domain.PageResult;
import org.yann.verulia.system.domain.dto.UserDtos;
import org.yann.verulia.system.domain.entity.SysUser;

/**
 * 用户 Service 接口
 *
 * @author Yann
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     *
     * @param queryDto 查询参数
     * @return 用户列表分页结果
     */
    PageResult<UserDtos.Result> pageQuery(UserDtos.Query queryDto);

    /**
     * 根据ID获取用户详情
     *
     * @param id 用户ID
     * @return 用户详情 VO
     */
    UserDtos.Result getUserById(Long id);

    /**
     * 新增用户
     *
     * @param createParams 新增参数
     */
    void addUser(UserDtos.Create createParams);

    /**
     * 修改用户
     *
     * @param updateParams 修改参数
     */
    void updateUser(UserDtos.Update updateParams);

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    void deleteUser(Long id);
}
