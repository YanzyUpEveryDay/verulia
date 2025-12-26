package org.yann.verulia.adventure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.yann.verulia.adventure.domain.entity.AdvTask;

/**
 * 奇遇任务 Mapper
 *
 * @author Yann
 */
@Mapper
public interface AdvTaskMapper extends BaseMapper<AdvTask> {
}
