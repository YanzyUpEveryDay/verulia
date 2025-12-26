package org.yann.verulia.adventure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.yann.verulia.adventure.domain.entity.AdvTask;

/**
 * 奇遇任务 Mapper
 *
 * @author Yann
 */
@Mapper
public interface AdvTaskMapper extends BaseMapper<AdvTask> {

    /**
     * 获取随机奇遇任务（数据库层面随机）
     * 使用ORDER BY RAND()在数据库层面实现随机，避免全量查询
     *
     * @param category 奇遇类型（可选）
     * @return 随机奇遇任务
     */
    @Select({"<script>",
            "SELECT * FROM adv_task",
            "WHERE status = '1' AND deleted = '0'",
            "<if test='category != null and category != \"\"'>",
            "AND category = #{category}",
            "</if>",
            "ORDER BY RAND() LIMIT 1",
            "</script>"})
    AdvTask selectRandomTask(@Param("category") String category);
}
