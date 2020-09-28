package com.ngu.product.server;

import com.ngu.product.domain.ProductCategory;

import java.util.List;

public interface CategoryService {

    List<ProductCategory> findByCategoryTypeIn(List<Integer> cateIntegerTypeList);
}
