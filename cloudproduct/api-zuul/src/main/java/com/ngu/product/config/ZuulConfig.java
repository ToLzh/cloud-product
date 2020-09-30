package com.ngu.product.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RefreshScope  // 刷新springcloud-config中最新得信息
@ConfigurationProperties("zuul")
public class ZuulConfig {

    @ConditionalOnMissingBean
    public ZuulProperties zuulProperties(){
        return new ZuulProperties();
    }
}
