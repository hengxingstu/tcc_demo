package com.hengxing.business.feign;

import com.hengxing.common.api.StockApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/11/2023 20:57:09
 */
@FeignClient("stock")
public interface StockFeign extends StockApi {

//    @PostMapping("stock/deduct")
//    ResponseBean deductStock(@RequestParam("pid") Integer pid, @RequestParam("count") Integer count);
}
