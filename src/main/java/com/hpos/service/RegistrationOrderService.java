package com.hpos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hpos.common.PageResult;
import com.hpos.entity.RegistrationOrder;
import com.hpos.dto.RegistrationRequest;
import com.hpos.dto.RegistrationVO;

/**
 * 挂号订单 Service 接口
 */
public interface RegistrationOrderService extends IService<RegistrationOrder> {

    String createOrder(RegistrationRequest request);

    PageResult<RegistrationVO> getPatientOrders(Integer patientId, int page, int size);

    void cancelOrder(Integer orderId, Integer patientId);
}
