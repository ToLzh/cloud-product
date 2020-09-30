package com.ngu.product.service.impl;

import com.ngu.product.dao.OrderDetailDao;
import com.ngu.product.dao.OrderMasterDao;
import com.ngu.product.dto.OrderDTO;
import com.ngu.product.entities.CartDTOOutPut;
import com.ngu.product.entities.OrderDetail;
import com.ngu.product.entities.OrderMaster;
import com.ngu.product.entities.ProductInfoOutPut;
import com.ngu.product.enums.OrderExceptionEnum;
import com.ngu.product.enums.OrderStatusEnum;
import com.ngu.product.enums.PayStatusEnum;
import com.ngu.product.exception.OrderException;
import com.ngu.product.service.OrderService;
import com.ngu.product.feignClient.ProductFeign;
import com.ngu.product.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMasterDao orderMasterDao;

    @Resource
    private OrderDetailDao orderDetailDao;

    @Resource
    private ProductFeign productFeign;

    /**
     * 创建订单
     * 优化：
     * 1. 商品不存在
     * 2. 其他服务模块异常
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.genUniqueKey();

        // 秒杀场景
        // 1.从redis中获取商品信息
        // 2. redis 减库存
        // 3. 若出现异常， redis 回复库存
        // 4 创建订单 写入数据库 ， 发消息队列 去减数据库库存

       // 2.商品查询
        List<String> productIdList = orderDTO.getOrderDetailList().stream()
                .map(OrderDetail::getProductId)
                .collect(Collectors.toList());
        List<ProductInfoOutPut> productInfoList = productFeign.findByProductIdIn(productIdList);
        // 3.计算总价
        List<OrderDetail> orderDetailList = orderDTO.getOrderDetailList();
        BigDecimal amount = new BigDecimal(BigInteger.ZERO);

        for (OrderDetail orderDetail : orderDetailList) {
            for (ProductInfoOutPut productInfo : productInfoList) {
                if (orderDetail.getProductId().equals(productInfo.getProductId())) {
                    amount = productInfo.getProductPrice()
                            .multiply(new BigDecimal(orderDetail.getProductQuantity()) )
                            .add(amount);

                    BeanUtils.copyProperties(productInfo,orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());
                    // 详情入库
                    orderDetailDao.save(orderDetail);
                }
            }
        }

        // 4.口库存
        List<CartDTOOutPut> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(x -> {
                   return new CartDTOOutPut(x.getProductId(), x.getProductQuantity());
                })
                .collect(Collectors.toList());
        productFeign.decreaseStock(cartDTOList);

        // 5.订单入库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(amount);
        orderMaster.setOrderId(orderDTO.getOrderId());
        orderMaster.setOrderStatus(OrderStatusEnum.New.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterDao.save(orderMaster);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(String orderId) {
        // 1.查询订单
        Optional<OrderMaster> masterOptional = orderMasterDao.findById(orderId);
        boolean present = masterOptional.isPresent();
        if (!present) {
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_EXIST);
        }
        // 2.判断状态
        OrderMaster orderMaster = masterOptional.get();
        if (orderMaster.getOrderStatus() != OrderStatusEnum.New.getCode()) {
            throw new OrderException(OrderExceptionEnum.ORDER_STATUS_ERROR);
        }
        // 3.修改订单为完结
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderMasterDao.save(orderMaster);

        List<OrderDetail> orderDetailList = orderDetailDao.findOrderDetailByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new OrderException(OrderExceptionEnum.ORDER_DETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
