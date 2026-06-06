package com.hpos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hpos.entity.RegistrationSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 号源 Mapper 接口
 */
@Mapper
public interface RegistrationSourceMapper extends BaseMapper<RegistrationSource> {

    /**
     * 原子扣减号源（防止超卖）
     * 
     * 在数据库层面完成扣减，先判断 available_count > 0 再扣，
     * 返回受影响行数，0 表示没抢到
     */
    @Update("UPDATE registration_source SET available_count = available_count - 1 " +
            "WHERE id = #{sourceId} AND available_count > 0")
    int deductSourceAtomic(@Param("sourceId") Integer sourceId);

    /**
     * 恢复号源（取消订单时调用）
     * 确保不超过总号数
     */
    @Update("UPDATE registration_source SET available_count = available_count + 1 " +
            "WHERE id = #{sourceId} AND available_count < total_count")
    int restoreSourceAtomic(@Param("sourceId") Integer sourceId);
}
