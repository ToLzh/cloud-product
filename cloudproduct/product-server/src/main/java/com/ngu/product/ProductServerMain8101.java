package com.ngu.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductServerMain8101 {
    public static void main(String[] args) {
        SpringApplication.run(ProductServerMain8101.class, args);
    }
}
