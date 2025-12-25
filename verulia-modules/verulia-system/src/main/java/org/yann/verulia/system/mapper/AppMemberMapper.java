package org.yann.verulia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.yann.verulia.system.domain.entity.AppMember;

/**
 * 会员 Mapper
 *
 * @author Yann
 */
@Mapper
public interface AppMemberMapper extends BaseMapper<AppMember> {
}
