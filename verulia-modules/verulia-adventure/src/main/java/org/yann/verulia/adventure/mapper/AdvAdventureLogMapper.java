package org.yann.verulia.adventure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.yann.verulia.adventure.domain.entity.AdvAdventureLog;

import java.time.LocalDate;

/**
 * 奇遇手账记录 Mapper
 *
 * @author Yann
 */
@Mapper
public interface AdvAdventureLogMapper extends BaseMapper<AdvAdventureLog> {

    /**
     * 查询用户指定日期的奇遇手账
     *
     * @param userId 用户ID
     * @param adventureDate 奇遇日期
     * @return 奇遇手账记录
     */
    @Select("SELECT * FROM adv_adventure_log WHERE user_id = #{userId} AND adventure_date = #{adventureDate} AND deleted = '0'")
    AdvAdventureLog selectByUserAndDate(@Param("userId") Long userId, @Param("adventureDate") LocalDate adventureDate);

    /**
     * 统计用户今日是否已有奇遇
     *
     * @param userId 用户ID
     * @param adventureDate 奇遇日期
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM adv_adventure_log WHERE user_id = #{userId} AND adventure_date = #{adventureDate} AND deleted = '0'")
    int countByUserAndDate(@Param("userId") Long userId, @Param("adventureDate") LocalDate adventureDate);
}
