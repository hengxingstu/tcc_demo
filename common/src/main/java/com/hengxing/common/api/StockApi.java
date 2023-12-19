package com.hengxing.common.api;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * stock远程接口
 * @author hengxing
 * @version 1.0
 * @project tcc_demo
 * @date 12/17/2023 18:31:34
 */
@LocalTCC
public interface StockApi {

    /**
     * 扣库存方法（一阶段方法）
     *
     * @return boolean
     * @author hengxing
     * @date 12/17/2023 18:42:29
     */
    @PostMapping("stock/deduct")
    @TwoPhaseBusinessAction(
            name = "stockApi",
            commitMethod = "commit",
            rollbackMethod = "rollback"
    )
    boolean deductStock(
            @RequestBody BusinessActionContext context,
            @RequestParam("pid")
            @BusinessActionContextParameter(paramName = "pid") Integer pid,
            @RequestParam("count")
            @BusinessActionContextParameter(paramName = "count") Integer count
    );

    /**
     * 提交方法（二阶段方法）
     *
     * @return boolean
     * @author hengxing
     * @date 12/17/2023 18:42:49
     */
    @PostMapping("stock/commit")
    boolean commit(@RequestBody BusinessActionContext context);

    /**
     * 回滚方法（二阶段方法）
     *
     * @return boolean
     * @author hengxing
     * @date 12/17/2023 18:43:10
     */
    @PostMapping("stock/rollback")
    boolean rollback(@RequestBody BusinessActionContext context);
}
