package com.ngu.product.service;

import com.ngu.product.dto.OrderDTO;

public interface OrderService {

    public OrderDTO create(OrderDTO orderDTO);

    /**
     * 只能卖家草纸uo
     * @param orderId
     * @return
     */
    OrderDTO finish(String orderId);
}
