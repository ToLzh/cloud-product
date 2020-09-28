package com.ngu.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProductClientMain8111 {
    public static void main(String[] args) {
        SpringApplication.run(ProductClientMain8111.class, args);
    }
}
