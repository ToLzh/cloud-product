package com.ngu.product.server.impl;

import com.ngu.product.dao.CategoryDao;
import com.ngu.product.domain.ProductCategory;
import com.ngu.product.server.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> cateIntegerTypeList) {
        return categoryDao.findByCategoryTypeIn(cateIntegerTypeList);
    }
}
