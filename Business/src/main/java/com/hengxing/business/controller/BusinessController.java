package com.hengxing.business.controller;

import com.hengxing.business.service.BusinessService;
import com.hengxing.common.response.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/11/2023 20:50:33
 */
@RestController
public class BusinessController {

    @Autowired
    BusinessService businessService;

    @PostMapping("buy")
    public ResponseBean buy(Integer pid, Integer count, String username) {
        return businessService.buy(pid, count, username);
    }
}
