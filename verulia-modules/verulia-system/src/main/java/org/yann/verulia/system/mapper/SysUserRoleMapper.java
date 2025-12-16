package org.yann.verulia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.yann.verulia.system.domain.entity.SysUserRole;

/**
 * 用户角色关联 Mapper
 *
 * @author Yann
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
}
