package com.hengxing.account.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hengxing.account.mapper.AccountMapper;
import com.hengxing.account.model.Account;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/10/2023 14:47:34
 */
@Service
public class AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    @Autowired
    AccountMapper accountMapper;

    public boolean deduct(String username, Double money) {
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().lambda()
                .eq(Account::getUsername, username)
        );
        if (Objects.isNull(account)) {
            LOGGER.error("{} 的账户不存在，预扣款失败！", username);
            throw new RuntimeException("账户不存在，预扣款失败！");
        }
        Double accountMoney = account.getMoney();
        if (accountMoney < money) {
            LOGGER.error("{} 的账户余额不足，预扣款失败！", username);
            throw new RuntimeException("账户余额不足，预扣款失败！");
        }
        Double balance = accountMoney - money;
        account.setMoney(balance);
        account.setFreezeMoney(account.getFreezeMoney() + money);
        boolean success = accountMapper.update(account,
                new LambdaQueryWrapper<Account>().eq(Account::getUsername, username)
        ) == 1;
        if (success) {
            LOGGER.error("已预扣取账户{}的{}元。", username, money);
        }
        return success;
    }

    public boolean commit(BusinessActionContext context) {
        String username = context.getActionContext("username").toString();
        Double money = ((BigDecimal) context.getActionContext("money")).doubleValue();
        Account account = accountMapper.selectOne(new LambdaQueryWrapper<Account>().eq(Account::getUsername, username));
        if (account.getFreezeMoney() < money) {
            LOGGER.error("余额不足，扣余额失败");
            return false;
        }
        account.setFreezeMoney(account.getFreezeMoney() - money);
        boolean success = accountMapper.update(
                account,
                new LambdaQueryWrapper<Account>().eq(Account::getUsername, username)
        ) == 1;
        if (success) {
            LOGGER.info("扣款成功。");
        }
        return success;
    }

    public boolean rollback(BusinessActionContext context) {
        String username = context.getActionContext("username").toString();
        Double money = ((BigDecimal) context.getActionContext("money")).doubleValue();
        Account account = accountMapper.selectOne(new LambdaQueryWrapper<Account>().eq(Account::getUsername, username));
        if (account.getFreezeMoney() < money) {
            LOGGER.error("余额不足，扣库存回滚失败");
            return false;
        }
        account.setFreezeMoney(account.getFreezeMoney() - money);
        account.setMoney(account.getFreezeMoney() + money);
        boolean success = accountMapper.update(account, new LambdaQueryWrapper<Account>().eq(Account::getUsername, username)) == 1;
        if (success) {
            LOGGER.info("扣款回退成功。");
        }
        return success;
    }
}
