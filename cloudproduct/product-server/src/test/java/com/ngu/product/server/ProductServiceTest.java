package com.ngu.product.server;

import com.ngu.product.domain.ProductInfo;
import com.ngu.product.dto.CartDTO;
import com.ngu.product.entities.ProductInfoOutPut;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void findProductByIds(){
        List<ProductInfoOutPut> byProductIdIn = productService.findByProductIdIn(Arrays.asList("164103465734242707"));
        Assert.assertTrue(byProductIdIn.size()>1);
    }

    @Test
    public void decreaseStock(){
//        CartDTO cartDTO = new CartDTO();
//        cartDTO.setProductId("164103465734242707");
//        cartDTO.setProductQuantity(2);
//        productService.decreaseStock(Arrays.asList(cartDTO));
    }

}