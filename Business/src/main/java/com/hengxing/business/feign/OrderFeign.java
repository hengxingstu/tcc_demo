package com.hengxing.business.feign;

import com.hengxing.common.api.OrderApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 订单远程调用
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/11/2023 23:24:37
 */
@FeignClient("order")
public interface OrderFeign extends OrderApi {

}
