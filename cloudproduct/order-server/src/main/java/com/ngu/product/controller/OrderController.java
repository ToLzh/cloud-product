package com.ngu.product.controller;

import com.ngu.product.VO.ResultVO;
import com.ngu.product.converter.OrderFormToOrderDTO;
import com.ngu.product.dto.OrderDTO;
import com.ngu.product.enums.OrderExceptionEnum;
import com.ngu.product.exception.OrderException;
import com.ngu.product.form.OrderForm;
import com.ngu.product.service.OrderService;
import com.ngu.product.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Slf4j
@RefreshScope   //刷新 git上 config的最新配置信息
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 创建订单
     * 1.参数校验
     * 2.商品查询
     * 3.计算总价
     * 4.口库存
     * 5.订单入库
     */
    @PostMapping("/create")
    public ResultVO create(@Valid OrderForm orderForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            log.error("[创建订单] 参数不正确, orderForm={}",orderForm);
            throw new OrderException(OrderExceptionEnum.PARAM_ERROR.getCode()
                    ,bindingResult.getFieldError().getDefaultMessage());
        }
        // orderForm -> orderDTO
        OrderDTO orderDTO = OrderFormToOrderDTO.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("[创建订单] 购物车信息为空");
            throw new OrderException(OrderExceptionEnum.CART_EMPTY);
        }

        OrderDTO result = orderService.create(orderDTO);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", result.getOrderId());

        return ResultVOUtil.success(map);
    }


    @PostMapping("finish")
    public ResultVO finish(@RequestParam("orderId") String orderId) {
        OrderDTO orderDTO = orderService.finish(orderId);
        return ResultVOUtil.success(orderDTO);
    }


}
