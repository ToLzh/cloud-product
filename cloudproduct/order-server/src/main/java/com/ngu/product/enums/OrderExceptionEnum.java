package com.ngu.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderExceptionEnum {
    PARAM_ERROR(1, "参数错误"),
    CART_EMPTY(2, "购物车为空"),
    ORDER_NOT_EXIST(3, "订单不存在"),
    ORDER_STATUS_ERROR(4,"订单状态有误"),
    ORDER_DETAIL_NOT_EXIST(5, "详情不存在"),
    ;
    private Integer code;
    private String msg;
}
