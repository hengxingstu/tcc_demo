package com.hengxing.stock.mapper;

import com.hengxing.stock.model.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/11/2023 20:27:07
 */
@Mapper
public interface StockMapper {
    @Select("select * from stock_tb where pid = #{pid}")
    Stock getStockById(Integer pid);

    @Update("update stock_tb set count = #{count} where pid=#{pid}")
    int updateStock(Stock stock);
}
