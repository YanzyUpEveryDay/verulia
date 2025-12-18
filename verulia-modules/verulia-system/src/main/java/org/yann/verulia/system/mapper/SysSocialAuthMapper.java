package org.yann.verulia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.yann.verulia.system.domain.entity.SysSocialAuth;

/**
 * 第三方认证 Mapper
 *
 * @author Yann
 */
@Mapper
public interface SysSocialAuthMapper extends BaseMapper<SysSocialAuth> {
}
