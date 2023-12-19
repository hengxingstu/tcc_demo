package com.hengxing.business.feign;

import com.hengxing.common.api.OrderApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/11/2023 23:24:37
 */
@FeignClient("order")
public interface OrderFeign extends OrderApi {
//    @PostMapping("order/create")
//    ResponseBean createOrder(@RequestParam("username") String username, @RequestParam("pid") Integer pid, @RequestParam("count") Integer count);
}
