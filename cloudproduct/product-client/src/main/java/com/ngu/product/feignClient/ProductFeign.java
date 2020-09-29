package com.ngu.product.feignClient;

import com.ngu.product.entities.CartDTOOutPut;
import com.ngu.product.entities.ProductInfoOutPut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@FeignClient("PRODUCT-SERVICE")
public interface ProductFeign {

    @PostMapping("/product/listForOrder")
    List<ProductInfoOutPut> findByProductIdIn(@RequestBody List<String> productIdList);

    @PostMapping("/product/decreaseStock")
    void decreaseStock(@RequestBody List<CartDTOOutPut> cartDTOList);
}