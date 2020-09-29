package com.ngu.product.message;

import com.alibaba.fastjson.JSONObject;
import com.ngu.product.entities.ProductInfoOutPut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductInfoRecevice {

    @RabbitListener(queuesToDeclare = @Queue("product-productInfo"))
    public void process(String message) {
        log.info("*********  ProductInfoRecevice： {} ",message);
        ProductInfoOutPut productInfoOutPut = JSONObject.parseObject(message, ProductInfoOutPut.class);
    }

}
