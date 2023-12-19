package com.hengxing.stock.service;

import com.hengxing.stock.mapper.StockMapper;
import com.hengxing.stock.model.Stock;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/11/2023 09:04:52
 */
@Service
public class StockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);
    @Autowired
    StockMapper stockMapper;

    /**
     * 一阶段工作
     *
     * @param pid
     * @param count
     * @return boolean
     * @author hengxing
     * @date 12/17/2023 23:40:06
     */
    public boolean deduct(Integer pid, Integer count) {
        Stock stock = stockMapper.getStockById(pid);
        if (Objects.isNull(stock)) {
            LOGGER.error("{}商品不存在，扣库存失败。", pid);
            throw new RuntimeException("商品不存在，预扣库存失败。");
        }
        if (stock.getCount() < count) {
            LOGGER.error("商品库存数量：{}，购买数量：{}，扣库存失败。", stock.getCount(), count);
            throw new RuntimeException("库存不足，预扣库存失败");
        }
        stock.setCount(stock.getCount() - count);
        stock.setFreezeCount(stock.getFreezeCount() + count);
        int result = stockMapper.updateStock(stock);

        boolean success = result == 1;
        if (success) {
            LOGGER.info("商品{}预扣库存成功。", pid);
        }
        return success;
    }

    /**
     * 二阶段的提交
     *
     * @return boolean
     * @author hengxing
     * @date 12/17/2023 23:40:17
     */
    public boolean commit(BusinessActionContext context) {
        // 先取出一阶段的两个参数
        int pid = Integer.parseInt(context.getActionContext("pid").toString());
        int count = Integer.parseInt(context.getActionContext("count").toString());
        Stock stock = stockMapper.getStockById(pid);
        if (stock.getFreezeCount() < count) {
            LOGGER.error("{} 商品扣库存失败。", pid);
        }
        stock.setFreezeCount(stock.getFreezeCount() - count);
        boolean success = stockMapper.updateStock(stock) == 1;
        if (success) LOGGER.info("{} 商品扣库存成功。", pid);
        return success;
    }

    public boolean rollback(BusinessActionContext context) {
        // 先取出一阶段的两个参数
        int pid = Integer.parseInt(context.getActionContext("pid").toString());
        int count = Integer.parseInt(context.getActionContext("count").toString());
        Stock stock = stockMapper.getStockById(pid);
        if (stock.getFreezeCount() < count) {
            LOGGER.error("{} 商品未预扣库存，无需回滚。", pid);
            return true;
        }
        stock.setFreezeCount(stock.getFreezeCount() - count);
        stock.setCount(stock.getCount() + count);
        boolean success = stockMapper.updateStock(stock) == 1;
        if (success) LOGGER.info("{} 商品扣库存回滚成功", pid);
        return success;
    }
}
