package com.hengxing.common.api;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * account接口类
 *
 * @author hengxing
 * @version 1.0
 * @project tcc_demo
 * @date 12/17/2023 18:24:23
 */
@LocalTCC
public interface AccountApi {

    @PostMapping("/account/deduct")
    @TwoPhaseBusinessAction(name = "accountApi")
    boolean deduct(
            @RequestBody BusinessActionContext context,
            @RequestParam("username")
            @BusinessActionContextParameter(paramName = "username") String username,
            @RequestParam("money")
            @BusinessActionContextParameter(paramName = "money") Double money
    );

    @PostMapping("/account/commit")
    boolean commit(@RequestBody BusinessActionContext context);

    @PostMapping("/account/rollback")
    boolean rollback(@RequestBody BusinessActionContext context);

}
