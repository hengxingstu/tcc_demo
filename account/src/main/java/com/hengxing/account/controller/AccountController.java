package com.hengxing.account.controller;

import com.hengxing.account.service.AccountService;
import com.hengxing.common.api.AccountApi;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * account控制层，实现远程api接口
 * @author hengxing
 * @version 1.0
 * @project tcc_demo
 * @date 12/10/2023 14:46:46
 */
@RestController
@RequestMapping("/account")
public class AccountController implements AccountApi {
    @Autowired
    AccountService accountService;


    @PostMapping("/deduct")
    @Override
    public boolean deduct(BusinessActionContext context, String username, Double money) {
        return accountService.deduct(username, money);
    }

    @Override
    public boolean commit(BusinessActionContext context) {
        return accountService.commit(context);
    }

    @Override
    public boolean rollback(BusinessActionContext context) {
        return accountService.rollback(context);
    }

    //    @PostMapping("/deduct")
//    public ResponseBean deduct(String username,Double money){
//        return accountService.deduct(username,money);
//    }

}
