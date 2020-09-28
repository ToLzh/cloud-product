package com.ngu.product.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ngu.product.dto.OrderDTO;
import com.ngu.product.entities.OrderDetail;
import com.ngu.product.enums.OrderExceptionEnum;
import com.ngu.product.enums.OrderStatusEnum;
import com.ngu.product.exception.OrderException;
import com.ngu.product.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderFormToOrderDTO {

    public static OrderDTO convert(OrderForm orderForm) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        orderDTO.setBuyerPhone(orderForm.getPhone());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        
        Gson gson = new Gson();
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        } catch (Exception e) {
            log.error("[json转换] 错误，string = {}", orderForm.getItems());
            throw new OrderException(OrderExceptionEnum.PARAM_ERROR.getCode(), e.getMessage());
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
