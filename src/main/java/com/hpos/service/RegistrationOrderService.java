package com.hpos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpos.entity.RegistrationOrder;
import com.hpos.dto.RegistrationRequest;
import com.hpos.dto.RegistrationVO;
import java.util.List;

/**
 * 挂号订单 Service 接口
 */
public interface RegistrationOrderService extends IService<RegistrationOrder> {

    /**
     * 创建挂号订单（核心业务方法）
     *
     * @param request 挂号请求参数
     * @return 订单号
     */
    String createOrder(RegistrationRequest request);

    /**
     * 查询患者的挂号记录
     *
     * @param patientId 患者ID
     * @return 挂号记录列表
     */
    List<RegistrationVO> getPatientOrders(Integer patientId);

    /**
     * 取消挂号订单
     *
     * @param orderId   订单ID
     * @param patientId 患者ID（验证订单归属）
     */
    void cancelOrder(Integer orderId, Integer patientId);
}
