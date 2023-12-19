package com.hengxing.business.service;

import com.hengxing.business.feign.OrderFeign;
import com.hengxing.business.feign.StockFeign;
import com.hengxing.common.response.ResponseBean;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/15/2023 09:05:10
 */
@Service
public class BusinessService {

    @Autowired
    OrderFeign orderFeign;
    @Autowired
    StockFeign stockFeign;

    @GlobalTransactional
    public ResponseBean buy(Integer pid, Integer count, String username) {
        // 获取分布式事务的全局id
        String xid = RootContext.getXID();
        BusinessActionContext context = new BusinessActionContext();
        context.setXid(xid);
        boolean success = stockFeign.deductStock(context,pid, count);
        if (success) {
            success = orderFeign.createOrder(context, username, pid, count);
        }
        if (success) return ResponseBean.OK("下单成功");
        return ResponseBean.ERROR("下单失败");
    }


}
