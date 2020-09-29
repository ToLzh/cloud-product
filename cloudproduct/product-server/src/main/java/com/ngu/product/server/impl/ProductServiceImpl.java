package com.ngu.product.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.ngu.product.dao.ProductDao;
import com.ngu.product.domain.ProductInfo;
import com.ngu.product.dto.CartDTO;
import com.ngu.product.entities.CartDTOOutPut;
import com.ngu.product.entities.ProductInfoOutPut;
import com.ngu.product.enums.ProductExceptionEnum;
import com.ngu.product.enums.ProductStatusEnum;
import com.ngu.product.exception.ProductException;
import com.ngu.product.server.ProductService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductDao productDao;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<ProductInfo> findUpAll() {
        return productDao.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public List<ProductInfoOutPut> findByProductIdIn(List<String> productIdList) {
        return productDao.findByProductIdIn(productIdList).stream()
                .map(e -> {
                    ProductInfoOutPut output = new ProductInfoOutPut();
                    BeanUtils.copyProperties(e, output);
                    return output;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTOOutPut> cartDTOList) {
        for (CartDTOOutPut cartDTO : cartDTOList) {
            Optional<ProductInfo> productInfoOptiona = productDao.findById(cartDTO.getProductId());

            // 判断商品是否存在
            if (!productInfoOptiona.isPresent()) {
                throw new ProductException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
            }

            ProductInfo productInfo = productInfoOptiona.get();
            // 判断库存
            int stock = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if (stock < 0) {
                throw new ProductException(ProductExceptionEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(stock);
            productDao.save(productInfo);

            //发送消息
            rabbitTemplate.convertAndSend("product-productInfo", JSONObject.toJSONString(productInfo) );
        }
    }
}
