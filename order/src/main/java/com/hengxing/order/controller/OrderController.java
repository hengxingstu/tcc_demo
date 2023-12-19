package com.hengxing.order.controller;

import com.hengxing.common.api.OrderApi;
import com.hengxing.order.service.OrderService;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * order控制层代码
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/10/2023 20:04:17
 */
@RestController
public class OrderController implements OrderApi {
    @Autowired
    OrderService orderService;

    @Override
    @PostMapping("order/create")
    public boolean createOrder(BusinessActionContext context, String username, Integer pid, Integer count) {
        return orderService.createOrder(context,username, pid, count);
    }

    @Override
    public boolean commit(BusinessActionContext context) {
        return orderService.commit(context);
    }

    @Override
    public boolean rollback(BusinessActionContext context) {
        return orderService.rollback(context);
    }
}
