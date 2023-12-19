package com.hengxing.order.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/10/2023 20:09:29
 */
@Mapper
public interface  OrderMapper {
    @Insert("insert into order_tb (username,pid,count,money) values (#{username},#{pid},#{count},#{money})")
    int createOrder(@Param("username") String username,
                     @Param("pid")  Integer pid,
                     @Param("count")  Integer count,
                     @Param("money")  Double money);
}
