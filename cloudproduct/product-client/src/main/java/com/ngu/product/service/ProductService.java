package com.ngu.product.service;

import com.ngu.product.domain.ProductInfo;
import com.ngu.product.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@FeignClient("product-service")
public interface ProductService {

    @PostMapping("/product/listForOrder")
    List<ProductInfo> findByProductIdIn(@RequestBody List<String> productIdList);

    @PostMapping("/product/decreaseStock")
    void decreaseStock(@RequestBody List<CartDTO> cartDTOList);
}