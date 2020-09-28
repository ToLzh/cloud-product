package com.ngu.product.service.impl;

import com.ngu.product.dao.OrderDetailDao;
import com.ngu.product.dao.OrderMasterDao;
import com.ngu.product.domain.ProductInfo;
import com.ngu.product.dto.CartDTO;
import com.ngu.product.dto.OrderDTO;
import com.ngu.product.entities.OrderDetail;
import com.ngu.product.entities.OrderMaster;
import com.ngu.product.enums.OrderStatusEnum;
import com.ngu.product.enums.PayStatusEnum;
import com.ngu.product.server.ProductService;
import com.ngu.product.service.OrderService;
import com.ngu.product.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMasterDao orderMasterDao;

    @Resource
    private OrderDetailDao orderDetailDao;

    @Resource
    private ProductService productService;

    /**
     * 创建订单
     * 优化：
     * 1. 商品不存在
     * 2. 其他服务模块异常
     * @param orderDTO
     * @return
     */
    @Override
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.genUniqueKey();

        // 2.商品查询
        List<String> productIdList = orderDTO.getOrderDetailList().stream()
                .map(OrderDetail::getProductId)
                .collect(Collectors.toList());
        List<ProductInfo> productInfoList = productService.findByProductIdIn(productIdList);
        // 3.计算总价
        List<OrderDetail> orderDetailList = orderDTO.getOrderDetailList();
        BigDecimal amount = new BigDecimal(BigInteger.ZERO);

        for (OrderDetail orderDetail : orderDetailList) {
            for (ProductInfo productInfo : productInfoList) {
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
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(x -> {
                   return new CartDTO(x.getProductId(), x.getProductQuantity());
                })
                .collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);

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
}
