package com.ngu.product.message;

import com.alibaba.fastjson.JSONObject;
import com.ngu.product.entities.ProductInfoOutPut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductInfoRecevice {

    private static final String PRODUCT_STOCK_TEMPLATE = "product_stock_%s";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RabbitListener(queuesToDeclare = @Queue("product-productInfo"))
    public void process(String message) {
        log.info("*********  ProductInfoRecevice： {} ",message);
        List<ProductInfoOutPut> productInfoOutPutList = JSONObject.parseArray(message, ProductInfoOutPut.class);

        // 存储到 redis 中
        productInfoOutPutList.forEach(productInfoOutPut->{
            redisTemplate.opsForValue().set(
                    String.format(PRODUCT_STOCK_TEMPLATE, productInfoOutPut.getProductId())
                    , productInfoOutPut.getProductStock().toString()
            );
        });
    }

}
