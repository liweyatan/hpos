package com.hpos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hpos.entity.RegistrationOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 挂号订单 Mapper 接口
 */
@Mapper
public interface RegistrationOrderMapper extends BaseMapper<RegistrationOrder> {
}
