package com.hengxing.common.api;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author hengxing
 * @version 1.0
 * @project tcc_demo
 * @date 12/17/2023 18:32:00
 */
@LocalTCC
public interface OrderApi {

    @PostMapping("order/create")
    @TwoPhaseBusinessAction(name = "orderApi")
    boolean createOrder(
            @RequestBody BusinessActionContext context,
            @RequestParam("username")
            @BusinessActionContextParameter(paramName = "username") String username,
            @RequestParam("pid")
            @BusinessActionContextParameter(paramName = "pid") Integer pid,
            @RequestParam("count")
            @BusinessActionContextParameter(paramName = "count") Integer count);

    @PostMapping("order/commit")
    boolean commit(BusinessActionContext context);

    @PostMapping("order/rollback")
    boolean rollback(BusinessActionContext context);
}
