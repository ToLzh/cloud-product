package com.ngu.product.server;

import com.ngu.product.domain.ProductInfo;
import com.ngu.product.dto.CartDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {

    /**
     *  查询所有在架商品列表
     */
    List<ProductInfo> findUpAll();

    /**
     * 根据id查商品列表
     * @param productIdList
     * @return
     */
    List<ProductInfo> findByProductIdIn(List<String> productIdList);

    /**
     * 删除库存 批量
     */
    void decreaseStock(List<CartDTO> cartDTOList);
}
