package com.hengxing.order.feign;

import com.hengxing.common.api.AccountApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/10/2023 20:42:27
 */
@FeignClient("account")
public interface AccountFeign extends AccountApi {

//    @PostMapping("account/deduct")
//    ResponseBean deduct(@RequestParam("username")String username, @RequestParam("money") Double money);
}
