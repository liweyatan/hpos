package com.hpos.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_QUEUE = "hpos.order.notification";

    @Bean
    public Queue orderNotificationQueue() {
        return new Queue(ORDER_QUEUE, true);
    }
}
