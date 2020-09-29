package com.ngu.product.controller;

import com.ngu.product.VO.ResultVO;
import com.ngu.product.domain.*;
import com.ngu.product.dto.CartDTO;
import com.ngu.product.entities.CartDTOOutPut;
import com.ngu.product.entities.ProductInfoOutPut;
import com.ngu.product.server.CategoryService;
import com.ngu.product.server.ProductService;
import com.ngu.product.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private CategoryService categoryService;

    /**
     * 1. 查询所有在架的商品
     * 2. 获取类目type列表
     * 3. 查询类目
     * 4. 构造数据
     */
    @GetMapping("/list")
    public ResultVO<ProductVO> list(){
        // 查询所有在架商品
        List<ProductInfo> productInfoList = productService.findUpAll();
        // 获取类目type列表
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(ProductInfo::getCategoryType)
                .collect(Collectors.toList());

        // 查询类目
        List<ProductCategory> categoryList = categoryService.findByCategoryTypeIn(categoryTypeList);

        // 构造数据
        List<ProductVO> productVOList = new ArrayList<ProductVO>();

        categoryList.forEach(x -> {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(x.getCategoryName());
            productVO.setCategoryType(x.getCategoryType());

            List<ProductInfoVO> productInfoVOList = new ArrayList<ProductInfoVO>();
            productInfoList.forEach(p->{
                if (productVO.getCategoryType().equals(x.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    // 内容拷贝，属性名一样
                    BeanUtils.copyProperties(p,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            });

            productVO.setProductInfoVoList(productInfoVOList);
            productVOList.add(productVO);
        });

        return ResultVOUtil.success(productVOList);
    }

    @PostMapping("/listForOrder")
    public List<ProductInfoOutPut> listForOrder(@RequestBody List<String> productIdList) {
        return productService.findByProductIdIn(productIdList);
    }

    @PostMapping("/decreaseStock")
    public void decreaseStock(@RequestBody List<CartDTOOutPut> cartDTOList) {
        log.info("*******进入 decreaseStock  ***********");
        productService.decreaseStock(cartDTOList);
        log.info("*******结束 decreaseStock  ***********");
    }
}

