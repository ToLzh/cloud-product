package com.ngu.product.exception;

import com.ngu.product.enums.ProductExceptionEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductException extends RuntimeException {
    private Integer code;
    private String msg;

    public ProductException(ProductExceptionEnum productExceptionEnum) {
        super(productExceptionEnum.getMsg());
        this.code = productExceptionEnum.getCode();
    }

}
