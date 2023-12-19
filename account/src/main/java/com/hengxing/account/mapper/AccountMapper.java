package com.hengxing.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hengxing.account.model.Account;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/10/2023 14:58:37
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
