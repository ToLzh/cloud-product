package com.ngu.product.controller;

import com.ngu.product.OrderServerMain8201;
import com.ngu.product.message.StreamClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OrderServerMain8201.class})
public class MqTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StreamClient streamClient;

    @Test
    public void work(){
        rabbitTemplate.convertAndSend("product-queue","测试发送");
    }

    @Test
    public void streamTest(){
        streamClient.output().send(MessageBuilder.withPayload("stream-测试发送").build());
    }
}
