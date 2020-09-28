package com.ngu.product.client;

import com.ngu.product.domain.ProductInfo;
import com.ngu.product.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@FeignClient("product-service")
public interface ProductClient {

    @PostMapping("/product/listForOrder")
    List<ProductInfo> findByProductIdIn(@RequestBody List<String> productIdList);

    @PostMapping("/product/decreaseStock")
    void decreaseStock(@RequestBody List<CartDTO> cartDTOList);
}
