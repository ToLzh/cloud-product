package com.ngu.product.dao;

import com.ngu.product.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailDao extends JpaRepository<OrderDetail,String> {

    List<OrderDetail> findOrderDetailByOrderId(String orderId);

}
