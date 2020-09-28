package com.ngu.product.exception;

import com.ngu.product.enums.OrderExceptionEnum;

public class OrderException extends RuntimeException {
    private Integer code;

    public OrderException(Integer code,String message){
        super(message);
        this.code = code;
    }

    public OrderException(OrderExceptionEnum orderExceptionEnum) {
        super(orderExceptionEnum.getMsg());
        this.code = orderExceptionEnum.getCode();
    }
}
