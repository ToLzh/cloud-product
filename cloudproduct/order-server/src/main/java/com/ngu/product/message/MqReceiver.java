package com.ngu.product.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * rabbitmq接收
 */
@Slf4j
@Component
public class MqReceiver {
    @RabbitListener(queuesToDeclare = @Queue("product-queue"))
    public void process(String message) {
        log.info("******* MqReceiver:{}",message);
    }
}
