package org.yann.verulia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.yann.verulia.system.domain.entity.SysUser;

/**
 * 用户 Mapper
 *
 * @author Yann
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
