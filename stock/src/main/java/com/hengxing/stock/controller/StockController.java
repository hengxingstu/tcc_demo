package com.hengxing.stock.controller;

import com.hengxing.common.api.StockApi;
import com.hengxing.stock.service.StockService;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/10/2023 21:10:56
 */
@RestController
public class StockController implements StockApi {
    @Autowired
    StockService stockService;

    @Override
    public boolean deductStock(BusinessActionContext context, Integer pid, Integer count) {
        return stockService.deduct(pid, count);
    }

    @Override
    public boolean commit(BusinessActionContext context) {
        return stockService.commit(context);
    }

    @Override
    public boolean rollback(BusinessActionContext context) {
        return stockService.rollback(context);
    }


//    @PostMapping("/stock/deduct")
//    public ResponseBean deductStock(Integer pid, Integer count) {
//        return stockService.deduct(pid,count);
//    }
}
