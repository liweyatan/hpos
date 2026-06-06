package com.hpos.rabbitmq;

import com.hpos.rabbitmq.RabbitMQConfig;
import com.hpos.service.OrderNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderNotificationServiceImpl implements OrderNotificationService {

    private static final Logger log = LoggerFactory.getLogger(OrderNotificationServiceImpl.class);

    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendOrderSuccessNotification(String orderNo, String patientName,
                                              String doctorName, String workDate, String periodText) {
        String message = String.format(
                "【挂号成功】患者%s，您已成功预约%s医生(%s %s)，订单号：%s",
                patientName, doctorName, workDate, periodText, orderNo);

        log.info("挂号通知: {}", message);

        if (rabbitTemplate != null) {
            try {
                rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, message);
                log.info("已发送挂号通知消息到队列: {}", orderNo);
            } catch (Exception e) {
                log.warn("RabbitMQ 不可用，通知暂存日志: {}", e.getMessage());
            }
        } else {
            log.info("（RabbitMQ 未配置，通知仅记录日志）");
        }
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleNotification(String message) {
        log.info("===== 挂号通知（模拟发送短信/邮件）=====");
        log.info(message);
        log.info("======================================");
    }
}
