package com.hengxing.order.service;

import com.hengxing.order.feign.AccountFeign;
import com.hengxing.order.mapper.OrderMapper;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/10/2023 20:07:17
 */
@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    AccountFeign accountFeign;

//    public ResponseBean createOrder(BusinessActionContext context,String username, Integer pid, Integer count) {
//        ResponseBean response = accountFeign.deduct(context,username, count * 100.0);
//        if (200 == response.getStatus()) {
//            //创建订单
//            int result = orderMapper.createOrder(username, pid, count, count * 100.0);
//            return result == 1 ?
//                    ResponseBean.OK("订单创建成功。") :
//                    ResponseBean.ERROR("订单创建失败。");
//        }
//        return response;
//
//    }

    /**
     * 下订单一共两步：1. 扣款 2. 添加订单
     * <p>
     *     一阶段直接扣款，扣款成功会返回true。此时会执行二阶段的commit方法，我们在commit方法中，创建订单
     * </p>
     * 此时如果一阶段扣款失败，再执行二阶段rollback方法，我们并没有创建订单，所以可以什么都不做
     * @param context
	 * @param username
	 * @param pid
	 * @param count
     * @return boolean
     * @author hengxing
     * @date 12/18/2023 21:34:18
     */
    public boolean createOrder(BusinessActionContext context, String username, Integer pid, Integer count) {
        boolean result = accountFeign.deduct(context, username, count * 100.0);
        return result;
    }

    public boolean commit(BusinessActionContext context) {
        String username = (String) context.getActionContext("username");
        Integer pid = (Integer) context.getActionContext("pid");
        Integer count = (Integer) context.getActionContext("count");
        int orderResult = orderMapper.createOrder(username, pid, count, count * 100.0);
        boolean success = orderResult == 1;
        if (success) LOGGER.info("订单创建成功");
        return success;
    }

    public boolean rollback(BusinessActionContext context) {
        LOGGER.info("订单回退成功");
        return true;
    }

}
