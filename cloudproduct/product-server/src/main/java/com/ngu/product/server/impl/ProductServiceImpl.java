package com.ngu.product.server.impl;

import com.ngu.product.dao.ProductDao;
import com.ngu.product.domain.ProductInfo;
import com.ngu.product.dto.CartDTO;
import com.ngu.product.enums.ProductExceptionEnum;
import com.ngu.product.enums.ProductStatusEnum;
import com.ngu.product.exception.ProductException;
import com.ngu.product.server.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductDao productDao;

    @Override
    public List<ProductInfo> findUpAll() {
        return productDao.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public List<ProductInfo> findByProductIdIn(List<String> productIdList) {
        return productDao.findByProductIdIn(productIdList);
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
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
        }
    }
}
