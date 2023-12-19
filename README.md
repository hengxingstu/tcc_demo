> Seata
> Seata is an open source distributed transaction solution that delivers high performance and easy to use distributed transaction services under a microservices architecture.
> Seata 是一款开源的分布式事务解决方案，致力于在微服务架构下提供高性能和简单易用的分布式事务服务。

有一些需要提前普及的概念，请先看这篇文章：
[分布式事务理论](https://www.yuque.com/hengxingtongxue/rc4tgo/mm0nbarfn3sbyo75)

## AT模式
![](https://cdn.nlark.com/yuque/0/2023/png/35698483/1702295740229-d688cade-cdd1-4b7d-8024-bcaee2d1367a.png#averageHue=%23040201&clientId=ud120b8d3-814d-4&from=paste&id=uc615cea5&originHeight=940&originWidth=1732&originalType=url&ratio=1&rotation=0&showTitle=false&status=done&style=none&taskId=uf8fd2535-ac7d-4aac-98d8-2d2111888de&title=)
![](https://cdn.nlark.com/yuque/0/2023/png/35698483/1702605383170-db0b2c78-3f42-41de-aee7-457beedfd929.png#averageHue=%23c0ae85&clientId=ua9f3f1dd-5bab-4&from=paste&id=u71f74681&originHeight=473&originWidth=868&originalType=url&ratio=1.25&rotation=0&showTitle=false&status=done&style=none&taskId=u3a861a1a-a3c4-4d96-b7a1-a6326059522&title=)
数据库准备
```sql
/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : at_account

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 17/12/2023 12:27:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_tb
-- ----------------------------
DROP TABLE IF EXISTS `account_tb`;
CREATE TABLE `account_tb`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `money` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account_tb
-- ----------------------------
INSERT INTO `account_tb` VALUES (1, 'hengxing', 98999);

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `branch_id` bigint NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
```
```sql
/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : at_order

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 17/12/2023 12:27:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_tb
-- ----------------------------
DROP TABLE IF EXISTS `order_tb`;
CREATE TABLE `order_tb`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pid` int NULL DEFAULT NULL,
  `count` int NULL DEFAULT NULL,
  `money` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_tb
-- ----------------------------
INSERT INTO `order_tb` VALUES (30, 'hengxing', 1, 10, 1000);

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `branch_id` bigint NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
```
```sql
/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : at_stock

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 17/12/2023 12:27:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for stock_tb
-- ----------------------------
DROP TABLE IF EXISTS `stock_tb`;
CREATE TABLE `stock_tb`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `pid` int NULL DEFAULT NULL,
  `count` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stock_tb
-- ----------------------------
INSERT INTO `stock_tb` VALUES (1, 1, 99980);

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `branch_id` bigint NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
```
### 公共部分
定义返回信息
```java
package com.hengxing.common.response;

import java.util.Objects;

/**
 * 返回信息公共类
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/10/2023 14:39:32
 */
public class ResponseBean {
    private Integer status;
    private String message;
    private Object data;


    public static ResponseBean OK(String message) {
        return new ResponseBean(200, message);
    }

    public static ResponseBean OK(String message, Object data) {
        return new ResponseBean(200, message, data);
    }

    public static ResponseBean ERROR(String message) {
        return new ResponseBean(500, message);
    }

    public static ResponseBean ERROR(String message, Object data) {
        return new ResponseBean(500, message, data);
    }

}
```
### 注册中心
这里采用eureka项目，这是配置文件，
```yaml
server:
  address: 8761
eureka:
  client:
    fetch-registry: false
    register-with-eureka: false
spring:
  application:
    name: eurekaServer
```
主类上加上注释
```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }

}
```

### 账户服务
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35698483/1702295378862-0e7f65b8-5742-4ce1-aace-28040bbb26e8.png#averageHue=%232d3137&clientId=ud120b8d3-814d-4&from=paste&height=459&id=uf88f6b97&originHeight=459&originWidth=345&originalType=binary&ratio=1&rotation=0&showTitle=false&size=30182&status=done&style=none&taskId=ub645a74c-9c7c-472e-9fd7-71e09c900ee&title=&width=345)
mapper接口，用来查询数据库
```java
@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
```
实体类account，注意写表名
```java
@TableName("account_tb")
public class Account {
    private Integer id;
    private String username;
    private Double money;
    // 省略一万行。。。
```
服务层
```java
@Service
public class AccountService {
    @Autowired
    AccountMapper accountMapper;

    public ResponseBean deduct(String username, Double money) {
        Account account = accountMapper.selectOne(new QueryWrapper<Account>().lambda()
                                                  .eq(Account::getUsername, username)
                                                 );
        if (Objects.isNull(account)) throw new RuntimeException("账户不存在！");
        Double accountMoney = account.getMoney();
        if (accountMoney < money) throw new RuntimeException("账户余额不足！");
        Double balance = accountMoney - money;
        account.setMoney(balance);
        accountMapper.update(account,
                             new LambdaQueryWrapper<Account>().eq(Account::getUsername,username)
                            );
        return ResponseBean.OK("已扣取账户" + username + "的" + money + "元。");
    }
}
```
控制层
```java
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("/deduct")
    public ResponseBean deduct(String username,Double money){
        try {
            return accountService.deduct(username,money);
        } catch (RuntimeException e) {
            return ResponseBean.ERROR(e.getMessage());
        }
    }
}
```

这个服务其实很简单，通过web项目，把服务接口暴露出去。这个是最底层的服务，不需要调用其他服务来完成，所以不需要openFeign远程调用

### 订单服务
order服务，需要调用account服务，如果用户余额扣减成功，就可以继续生成订单。
调用account服务时，用的是远程调用，这里使用openFeign。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35698483/1702296487718-1dba2af1-2c5b-43d0-9367-08c0ca28d6d2.png#averageHue=%232d3137&clientId=ud120b8d3-814d-4&from=paste&height=452&id=ud09b9bcc&originHeight=452&originWidth=324&originalType=binary&ratio=1&rotation=0&showTitle=false&size=23592&status=done&style=none&taskId=u5f5beba3-3333-44ef-9a7e-a2d6def900f&title=&width=324)

mapper接口查询，修改数据
```java
@Mapper
public interface  OrderMapper {
    @Insert("insert into order_tb (username,pid,count,money) values (#{username},#{pid},#{count},#{money})")
    int createOrder(@Param("username") String username,
                     @Param("pid")  Integer pid,
                     @Param("count")  Integer count,
                     @Param("money")  Double money);
}
```
远程调用代码
```java
@FeignClient("account")
public interface AccountFeign {

    @PostMapping("account/deduct")
    ResponseBean deduct(@RequestParam("username")String username, @RequestParam("money") Double money);
}
```
> 注意，这里参数上的`@RequestParam()`不能被省略

另外，记得在主类上加注解`@EnableFeignClients`以打开远程调用的功能

service层具体业务，如果远程扣减余额成功，则可以生成订单
```java
@Service
public class OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    AccountFeign accountFeign;

    public ResponseBean createOrder(String username, Integer pid, Integer count) {
        ResponseBean response = accountFeign.deduct(username, count * 100.0);
        if (200 == response.getStatus()) {
            //创建订单
            int result = orderMapper.createOrder(username, pid, count, count * 100.0);
            return result == 1 ?
            ResponseBean.OK("订单创建成功。") :
            ResponseBean.ERROR("订单创建失败。");
        }
        return response;

    }

}
```
controller层直接调用
```java
@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("order/create")
    public ResponseBean createOrder(String username, Integer pid, Integer count){
        return orderService.createOrder(username, pid, count);
    }
}
```
### 库存服务
库存服务只需要操作本地的数据库即可，不需要远程调用，只要远程调用时，调用这个服务就可以了。
库存实体
```java
public class Stock {
    private Integer id;
    private Integer pid;
    private Integer count;
    // 省略一万行。。。
```
mapper
```java
@Mapper
public interface StockMapper {
    @Select("select * from stock_tb where pid = #{pid}")
    Stock getStockById(Integer pid);

    @Update("update stock_tb set count = #{count} where pid=#{pid}")
    int updateStock(Stock stock);
}
```
service
```java
@Service
public class StockService {
    @Autowired
    StockMapper stockMapper;

    public ResponseBean deduct(Integer pid, Integer count){
        Stock stock = stockMapper.getStockById(pid);
        if (Objects.isNull(stock)) {
            throw new RuntimeException("商品不存在，扣库存失败。");
        }
        if (stock.getCount() < count) {
            throw new RuntimeException("库存不足，扣库存失败");
        }
        stock.setCount(stock.getCount() - count);
        int result = stockMapper.updateStock(stock);

        return result == 1 ?
        ResponseBean.OK("扣库存成功"):
        ResponseBean.ERROR("扣库存失败");
    }
}
```
controller
```java
@RestController
public class StockController {
    @Autowired
    StockService stockService;

    @PostMapping("/stock/deduct")
    public ResponseBean deductStock(Integer pid, Integer count) {
        return stockService.deduct(pid,count);
    }
}
```

### 主业务
主业务需要与其他业务进行交互，所以需要远程调用。
注册到eureka，所以需要eureka组件。
需要分布式事务框架，seata。（可以暂时不添加）

远程调用订单生成，以及库存扣减
```java
@FeignClient("order")
public interface OrderFeign {
    @PostMapping("order/create")
    ResponseBean createOrder(@RequestParam("username") String username, @RequestParam("pid") Integer pid, @RequestParam("count") Integer count);
}
@FeignClient("stock")
public interface StockFeign {

    @PostMapping("stock/deduct")
    ResponseBean deductStock(@RequestParam("pid") Integer pid, @RequestParam("count") Integer count);
}
```
控制层中直接调用，和调用service一样
```java
@RestController
public class BusinessController {
    @Autowired
    OrderFeign orderFeign;
    @Autowired
    StockFeign stockFeign;

    @PostMapping("buy")
    public ResponseBean buy(Integer pid, Integer count, String username) {
        ResponseBean deduct = orderFeign.createOrder(username, pid, count);
        if (200 == deduct.getStatus()) {
            return stockFeign.deductStock(pid, count);
        }
        return deduct;
    }
}
```

主类上增加远程调用和eureka开启的注解
```java
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class BusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }

}
```

### 增加全局异常捕获器
在common公共部分中，增加一个异常捕获器

```java
@RestControllerAdvice
public class GlobalException {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseBean runtimeException(Exception e){
        return ResponseBean.ERROR(e.getMessage());
    }
}
```
我需要解释一下`@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)`这个注解。
一般情况下，openFeign在远程调用时，如果调用成功，对方返回httpStatus=200，而调用失败则返回500。这样，OpenFeign就知道调用失败从而回滚。
现在，我们通过全局异常捕获器将异常捕获，然后返回了自定义的ResponseBean信息，可是我们自定义的时候，并没有控制httpStatus信息，OpenFeign便不知道发生了异常。
所以这个注解就是在捕获异常时，将返回信息的httpStatus改为内部异常的状态。


### 增加全局事务
#### 项目增加依赖
那个服务需要全局事务，就在哪个服务上增加依赖
```xml
<properties>
    <spring-cloud-alibaba.version>2021.0.5.0</spring-cloud-alibaba.version>
</properties>
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
</dependency>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>${spring-cloud-alibaba.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```
给需要增加事务的方法加上注释
```java
@GlobalTransactional
public ResponseBean buy(Integer pid, Integer count, String username) {
    ResponseBean deduct = stockFeign.deductStock(pid, count);
    if (200 == deduct.getStatus()) {
        return orderFeign.createOrder(username, pid, count);
    }
    return deduct;
}
```

#### 配置并开启TC
现在是重头戏，我们先回顾一下分布式事务架构。
![](https://cdn.nlark.com/yuque/0/2023/png/35698483/1702605374937-42fcfdae-4600-47dd-b4e7-2da3b4ab9abd.png#averageHue=%23c0ae85&clientId=ua9f3f1dd-5bab-4&from=paste&id=u684f2dd8&originHeight=473&originWidth=868&originalType=url&ratio=1.25&rotation=0&showTitle=false&status=done&style=none&taskId=u594d1b92-8473-4f1f-88c1-c5d961f7d79&title=)
在 Seata 分布式事务中，TM、RM 和 TC 是 Seata 中三个重要的组件：

1. TM（Transaction Manager） - 事务管理器：
- TM 负责全局事务的创建、提交、回滚等操作。
- 在 Seata 中，TM 通常由业务系统提供或集成，用于管理全局事务。
2. RM（Resource Manager） - 资源管理器：
- RM 管理着本地事务资源，负责控制本地事务的提交和回滚。
- 在 Seata 中，每个涉及到的分支事务都对应一个 RM，用于管理与特定资源（如数据库、消息队列等）相关的本地事务。
3. TC（Transaction Coordinator） - 事务协调器：
- TC 是 Seata 中的核心组件，负责全局事务的协调和控制。
- 它协调各个 RM 完成本地事务，控制全局事务的提交和回滚。

这三个组件协同工作，实现了 Seata 的分布式事务管理功能。TM 负责全局事务的生命周期，RM 管理与特定资源相关的本地事务，而 TC 负责协调和控制全局事务的状态。

开启TC
去官网下载包
[GitHub - seata/seata: :fire: Seata is an easy-to-use, high-performance, open source distributed transaction solution.](https://github.com/seata/seata)
包下载之后，通过bin目录下的`seata-server.bat`文件启动，这里要注意，本地的JDK版本不要太高，如果JDK版本太高，打开会闪退。用JDK8就好。
> 提示：用cmd打开，可以看到报错信息。

看到服务控制台打印出注册成功的消息，就表示我们的服务已经准备好了。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35698483/1702722264339-99042f8d-36ca-4474-9e92-f346d95baeb9.png#averageHue=%2322262b&clientId=ud4af4f9d-4896-4&from=paste&height=167&id=u0a8cb62f&originHeight=209&originWidth=1755&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=75985&status=done&style=none&taskId=u8f73b8c7-d94a-4a36-a776-16ce91b161f&title=&width=1404)
然后发送请求，如果事务中遇到失败的情况，会立刻回滚，我们可以在代码中打个断点看下undo_log表中的情况，比如在事务中扣库存之后打上断点，就可以看到库存事务执行时的日志信息。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35698483/1702723932682-f44969f0-02ec-4c25-beee-10d825a184ad.png#averageHue=%23202125&clientId=uf75dd3bf-b554-4&from=paste&height=305&id=u67f9afb9&originHeight=381&originWidth=1220&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=56764&status=done&style=none&taskId=u5b758925-ddbd-4974-914f-7f7cee68364&title=&width=976)
这是库存事务信息
```json
    {
        "@class": "io.seata.rm.datasource.undo.BranchUndoLog",
        "xid": "192.168.59.1:8091:4143779775299407879",
        "branchId": 4143779775299407880,
        "sqlUndoLogs": [
            "java.util.ArrayList",
            [
                {
                    "@class": "io.seata.rm.datasource.undo.SQLUndoLog",
                    "sqlType": "UPDATE",
                    "tableName": "stock_tb",
                    "beforeImage": {
                        "@class": "io.seata.rm.datasource.sql.struct.TableRecords",
                        "tableName": "stock_tb",
                        "rows": [
                            "java.util.ArrayList",
                            [
                                {
                                    "@class": "io.seata.rm.datasource.sql.struct.Row",
                                    "fields": [
                                        "java.util.ArrayList",
                                        [
                                            {
                                                "@class": "io.seata.rm.datasource.sql.struct.Field",
                                                "name": "id",
                                                "keyType": "PRIMARY_KEY",
                                                "type": 4,
                                                "value": 1
                                            },
                                            {
                                                "@class": "io.seata.rm.datasource.sql.struct.Field",
                                                "name": "count",
                                                "keyType": "NULL",
                                                "type": 4,
                                                "value": 99980
                                            }
                                        ]
                                    ]
                                }
                            ]
                        ]
                    },
                    "afterImage": {
                        "@class": "io.seata.rm.datasource.sql.struct.TableRecords",
                        "tableName": "stock_tb",
                        "rows": [
                            "java.util.ArrayList",
                            [
                                {
                                    "@class": "io.seata.rm.datasource.sql.struct.Row",
                                    "fields": [
                                        "java.util.ArrayList",
                                        [
                                            {
                                                "@class": "io.seata.rm.datasource.sql.struct.Field",
                                                "name": "id",
                                                "keyType": "PRIMARY_KEY",
                                                "type": 4,
                                                "value": 1
                                            },
                                            {
                                                "@class": "io.seata.rm.datasource.sql.struct.Field",
                                                "name": "count",
                                                "keyType": "NULL",
                                                "type": 4,
                                                "value": 99970
                                            }
                                        ]
                                    ]
                                }
                            ]
                        ]
                    }
                }
            ]
        ]
    }
```
beforeImage是事务执行前的数据
afterImage是事务执行后的数据
可以看到这些信息中完整记录的数据在事务发生前后的样子，如果后续的事务失败，TC发来了回滚的命令，我们根据这个信息把数据还原，这就是分布式事务回滚时的反向补偿机制
## XA模式
### 项目代码
迁移项目代码到新项目，改个名字就好了，改为`xa_demo`
```java
package com.hengxing.account.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.xa.DataSourceProxyXA;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 设置XA 数据源
 * @author hengxing
 * @version 1.0
 * @project xa_demo
 * @date 12/16/2023 23:13:16
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    DruidDataSource druidDataSource(){
        return new DruidDataSource();
    }

    /**
     * 把普通的dataSource封装成一个XA DataSource
     *        
     * @return javax.sql.DataSource
     * @author hengxing
     * @date 12/16/2023 23:26:02
     */
    @Bean
    DataSource druidDataSourceXA(){
        return new DataSourceProxyXA(druidDataSource());
    }

	@Bean
    MybatisSqlSessionFactoryBean sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(druidDataSourceXA());
        factoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
        return factoryBean;
}
```
这三个部分其实做的是一件事：设置xa数据源，把普通的数据源替换成XA数据源，注意到这里的我们也同时配置了事务处理的factory。DataSourceProxyXA是我们配置的数据源代理，这个是XA模式代理的数据源，SEATA通过这个代理数据源来管理数据库。
> 这里有个报错需要注意，注入sqlSessionFactory的时候，不能用原生的sqlSessionFactory，会报错 `invalid bound statement (not found)`
> 替换成`MybatisSqlSessionFactoryBean`即可

另外，spring启动时会默认帮我们配置数据库源，既然我们自行配置了，就不需要springboot自动配置的内容（有可能影响我们的配置，导致不生效），我们在主类的注释上声明排序自动配置。
```java
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//排除数据库自动配置
@EnableEurekaClient
@ComponentScan(basePackages = {"com.hengxing.common.exception","com.hengxing.account"})
public class AccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }

}
```

给每个涉及到数据交互的服务，都加上这个配置类。
关闭数据源自动代理，我们刚刚配置了一个XA的数据源代理，不能被自动配置的数据源代理覆盖了。
```yaml
seata:
  enable-auto-data-source-proxy: false
```
### 数据库准备
```sql
/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : xa_account

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 17/12/2023 12:30:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_tb
-- ----------------------------
DROP TABLE IF EXISTS `account_tb`;
CREATE TABLE `account_tb`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `money` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account_tb
-- ----------------------------
INSERT INTO `account_tb` VALUES (1, 'hengxing', 10000);

SET FOREIGN_KEY_CHECKS = 1;
```
```sql
/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : xa_order

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 17/12/2023 12:30:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_tb
-- ----------------------------
DROP TABLE IF EXISTS `order_tb`;
CREATE TABLE `order_tb`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pid` int NULL DEFAULT NULL,
  `count` int NULL DEFAULT NULL,
  `money` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_tb
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
```
```sql
/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : xa_stock

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 17/12/2023 12:30:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for stock_tb
-- ----------------------------
DROP TABLE IF EXISTS `stock_tb`;
CREATE TABLE `stock_tb`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `pid` int NULL DEFAULT NULL,
  `count` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stock_tb
-- ----------------------------
INSERT INTO `stock_tb` VALUES (1, 1, 99990);

SET FOREIGN_KEY_CHECKS = 1;
```
这次的数据库中缺少了undo_log表，这个是为了防止XA配置失败，SEATA继续使用AT模式，如果我们没有undo_log表，使用AT模式就会报错，这样就不会让我们白费功夫排错了。
修改项目中的数据库连接配置
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 123456
    url: jdbc:mysql://localhost:3306/xa_account?characterEncoding=utf-8
```
发送请求，可以在控制台看到打印出的事务信息。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35698483/1702793755951-1bb2a113-4a2d-4f6f-8c73-36c1f38eeae0.png#averageHue=%23202428&clientId=u3103b67a-b4e1-4&from=paste&height=168&id=u8b6ea110&originHeight=210&originWidth=1781&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=56409&status=done&style=none&taskId=u9a1f5553-19e5-4d5c-9cf6-1f736946719&title=&width=1424.8)

## TCC模式
按照刚才迁移项目的方法，再迁移一个tcc_demo出来
### 数据库准备
```sql
/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : tcc_account

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 19/12/2023 13:45:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_tb
-- ----------------------------
DROP TABLE IF EXISTS `account_tb`;
CREATE TABLE `account_tb`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `money` double NULL DEFAULT NULL,
  `freezeMoney` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account_tb
-- ----------------------------
INSERT INTO `account_tb` VALUES (1, 'hengxing', 10000, 0);

SET FOREIGN_KEY_CHECKS = 1;

```
```sql
/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : tcc_order

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 19/12/2023 13:45:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_tb
-- ----------------------------
DROP TABLE IF EXISTS `order_tb`;
CREATE TABLE `order_tb`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pid` int NULL DEFAULT NULL,
  `count` int NULL DEFAULT NULL,
  `money` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_tb
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

```
```sql
/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : tcc_stock

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 19/12/2023 13:45:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for stock_tb
-- ----------------------------
DROP TABLE IF EXISTS `stock_tb`;
CREATE TABLE `stock_tb`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `pid` int NULL DEFAULT NULL,
  `count` int NULL DEFAULT NULL,
  `freezeCount` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stock_tb
-- ----------------------------
INSERT INTO `stock_tb` VALUES (1, 1, 100, 0);

SET FOREIGN_KEY_CHECKS = 1;

```
accout 和 stock 两张表中，分别增加了两个字段，freezeMoney 和 freezeCount 用来在一阶段和二阶段中冻结资金和库存。

目前的SEATA中，如果使用TCC模式，必须使用openFeign的继承特性去调用，之前的声明式调用会有问题。
为公共部分增加API 接口
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.hengxing</groupId>
  <artifactId>common</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <name>common</name>
  <description>common</description>
  <properties>
    <java.version>1.8</java.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <spring-boot.version>2.6.13</spring-boot.version>
    <spring-cloud.version>2021.0.5</spring-cloud.version>
    <spring-cloud-alibaba.version>2021.0.5.0</spring-cloud-alibaba.version>
  </properties>
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
    </dependency>
  </dependencies>
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring-boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
      <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-alibaba-dependencies</artifactId>
        <version>${spring-cloud-alibaba.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.1</version>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
          <encoding>UTF-8</encoding>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>${spring-boot.version}</version>
        <configuration>
                    <mainClass>com.hengxing.common.CommonApplication</mainClass>
                    <skip>true</skip>
                </configuration>
                <executions>
                    <execution>
                        <id>repackage</id>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>
```
### 准备接口类模板
#### 公共接口部分
common共享项目代码中，增加api接口类
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35698483/1702965388059-a061821c-8c42-4c98-9bd5-e639387a1d08.png#averageHue=%232d2f33&clientId=u0a317774-f137-4&from=paste&height=329&id=uceef864b&originHeight=411&originWidth=366&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=24741&status=done&style=none&taskId=ubdd63bd8-7f0e-41e4-8427-38c494916b9&title=&width=292.8)
```java
package com.hengxing.common.api;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * stock远程接口
 * @author hengxing
 * @version 1.0
 * @project tcc_demo
 * @date 12/17/2023 18:31:34
 */
@LocalTCC
public interface StockApi {

    /**
     * 扣库存方法（一阶段方法）
     *
     * @return boolean
     * @author hengxing
     * @date 12/17/2023 18:42:29
     */
    @PostMapping("stock/deduct")
    @TwoPhaseBusinessAction(
            name = "stockApi",
            commitMethod = "commit",
            rollbackMethod = "rollback"
    )
    boolean deductStock(
            @RequestBody BusinessActionContext context,
            @RequestParam("pid")
            @BusinessActionContextParameter(paramName = "pid") Integer pid,
            @RequestParam("count")
            @BusinessActionContextParameter(paramName = "count") Integer count
    );

    /**
     * 提交方法（二阶段方法）
     *
     * @return boolean
     * @author hengxing
     * @date 12/17/2023 18:42:49
     */
    @PostMapping("stock/commit")
    boolean commit(@RequestBody BusinessActionContext context);

    /**
     * 回滚方法（二阶段方法）
     *
     * @return boolean
     * @author hengxing
     * @date 12/17/2023 18:43:10
     */
    @PostMapping("stock/rollback")
    boolean rollback(@RequestBody BusinessActionContext context);
}

```
`@TwoPhaseBusinessAction()`用来标记该方法是一个一阶段方法，其中 commitMethod 和 rollbackMethod 定义了提交和撤回操作的名字。
执行过程中，一阶段的方法是开发者手动调用，调用完成之后，如果一阶段的方法返回值是true，就表示执行成功，SEATA会将执行成功这个事情报告给TC，等所有微服务一阶段都执行完毕的时候，TC会通知所有服务执行二阶段提交方法（失败会执行回退方法）。
这是操作的详细步骤：
> 一阶段操作：
> 1. 检查商品是否存在
> 2. 检查库存是否足够
> 3. 冻结库存（从count中减去库存，往freezeCount中加上库存
> 
二阶段操作：
> - 提交
>    1. 从freezeCount中抹去冻结的库存
> - 回滚
>    1. 从freezeCount中抹去冻结的库存
>    2. 恢复count的库存

我们再来看方法的参数，一阶段方法是接口，上面有方法参数，但是二阶段方法也需要这个参数来进行回退或者提交，要怎么做呢？
在接口方法中，`@BusinessActionContextParameter(paramName = "pid")`注释上参数，这个参数就会被加入action上下文中，这样在二阶段方法中，就可以通过`getActionContext(String key)`来调用，其中，key就是注释中的参数名。
ActionContext需要在一阶段中传进去。而且最终在调用这些远程方法时，需要传入一个全局id，这里先不考虑，等最后添加。
最后，注意到commit方法上也被加上了`@PostMapping()`注释，这是因为commit方法在调用的时候也是通过远程调用来触发的，所以也要加上注解。
同理增加accountApi和orderApi
```java
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
```
```java
package com.hengxing.common.api;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * order接口类
 * @author hengxing
 * @version 1.0
 * @project tcc_demo
 * @date 12/17/2023 18:32:00
 */
@LocalTCC
public interface OrderApi {

    @PostMapping("order/create")
    @TwoPhaseBusinessAction(name = "orderApi")
    boolean createOrder(
        @RequestBody BusinessActionContext context,
        @RequestParam("username")
        @BusinessActionContextParameter(paramName = "username") String username,
        @RequestParam("pid")
        @BusinessActionContextParameter(paramName = "pid") Integer pid,
        @RequestParam("count")
        @BusinessActionContextParameter(paramName = "count") Integer count);

    @PostMapping("order/commit")
    boolean commit(BusinessActionContext context);

    @PostMapping("order/rollback")
    boolean rollback(BusinessActionContext context);
}
```
#### stock业务
对于stock业务来说，我们需要完成api的具体逻辑，继承对应的接口即可
```java
package com.hengxing.stock.controller;

import com.hengxing.common.api.StockApi;
import com.hengxing.stock.service.StockService;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController implements StockApi {
    @Autowired
    StockService stockService;

    @Override
    public boolean deductStock(BusinessActionContext context, Integer pid, Integer count) {
        return stockService.deduct(pid, count);
    }

    @Override
    public boolean commit(BusinessActionContext context) {
        return stockService.commit(context);
    }

    @Override
    public boolean rollback(BusinessActionContext context) {
        return stockService.rollback(context);
    }

}
```
由于修改了库存实体，所以也修改实体类
```java
package com.hengxing.stock.model;

import java.util.Objects;

/**
 * 库存实体类
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/10/2023 21:06:03
 */
public class Stock {
    private Integer id;
    private Integer pid;
    private Integer count;
    private Integer freezeCount;

}
```
库存服务类
```java
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
 * 库存服务
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
```
> 注意一下rollback时如果判断了冻结库存小于需要扣减的库存，这是上面一阶段检测到库存不足时直接跳出了方法，所以没有冻结库存。既然此时没有冻结库存，也就不需要回滚了，直接返回回滚成功即可。如果返回失败，程序就会一直重试回滚，导致死锁。

再修改配置中的数据地址。
```yaml
spring:
  application:
    name: stock
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 123456
    url: jdbc:mysql://localhost:3306/tcc_stock?characterEncoding=utf-8
```
#### account业务
修改控制层代码
```java
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

```
修改账户实体类
```java
package com.hengxing.account.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Objects;

/**
 * 账户实体类
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/10/2023 16:30:29
 */
@TableName("account_tb")
public class Account {
    private Integer id;
    private String username;
    private Double money;
    @TableField("freezeMoney")
    private Double freezeMoney;

}
```
修改配置中的数据库地址
```yaml
spring:
  application:
    name: account
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 123456
    url: jdbc:mysql://localhost:3306/tcc_account?characterEncoding=utf-8
```
#### order业务
修改控制层代码
```java
package com.hengxing.order.controller;

import com.hengxing.common.api.OrderApi;
import com.hengxing.order.service.OrderService;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * order控制层代码
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/10/2023 20:04:17
 */
@RestController
public class OrderController implements OrderApi {
    @Autowired
    OrderService orderService;

    @Override
    @PostMapping("order/create")
    public boolean createOrder(BusinessActionContext context, String username, Integer pid, Integer count) {
        return orderService.createOrder(context,username, pid, count);
    }

    @Override
    public boolean commit(BusinessActionContext context) {
        return orderService.commit(context);
    }

    @Override
    public boolean rollback(BusinessActionContext context) {
        return orderService.rollback(context);
    }
}
```
order业务中，远程调用了account的代码，直接继承远程接口类即可
```java
package com.hengxing.order.feign;

import com.hengxing.common.api.AccountApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * account远程调用接口
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/10/2023 20:42:27
 */
@FeignClient("account")
public interface AccountFeign extends AccountApi {
}

```
服务类具体实现，订单业务稍有不同的地方是，我们在一阶段只需要扣减库存，二阶段确认其他服务都执行成功后，再创建订单。所以回滚的时候其实不需要任何操作
```java
package com.hengxing.order.service;

import com.hengxing.order.feign.AccountFeign;
import com.hengxing.order.mapper.OrderMapper;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单服务类实现
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/10/2023 20:07:17
 */
@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    AccountFeign accountFeign;

    /**
     * 下订单一共两步：1. 扣款 2. 添加订单
     * <p>
     *     一阶段直接扣款，扣款成功会返回true。此时会执行二阶段的commit方法，我们在commit方法中，创建订单
     * </p>
     * 此时如果一阶段扣款失败，再执行二阶段rollback方法，我们并没有创建订单，所以可以什么都不做
     * @param context
     * @param username
     * @param pid
     * @param count
     * @return boolean
     * @author hengxing
     * @date 12/18/2023 21:34:18
     */
    public boolean createOrder(BusinessActionContext context, String username, Integer pid, Integer count) {
        boolean result = accountFeign.deduct(context, username, count * 100.0);
        return result;
    }

    public boolean commit(BusinessActionContext context) {
        String username = (String) context.getActionContext("username");
        Integer pid = (Integer) context.getActionContext("pid");
        Integer count = (Integer) context.getActionContext("count");
        int orderResult = orderMapper.createOrder(username, pid, count, count * 100.0);
        boolean success = orderResult == 1;
        if (success) LOGGER.info("订单创建成功");
        return success;
    }

    public boolean rollback(BusinessActionContext context) {
        LOGGER.info("订单回退成功");
        return true;
    }

}
```
#### business业务
作为服务的调用者
需要添加上分布式事务的上下文，上下文中加上分布式事务的全局id
```java
package com.hengxing.business.service;

import com.hengxing.business.feign.OrderFeign;
import com.hengxing.business.feign.StockFeign;
import com.hengxing.common.response.ResponseBean;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商城的服务类
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/15/2023 09:05:10
 */
@Service
public class BusinessService {

    @Autowired
    OrderFeign orderFeign;
    @Autowired
    StockFeign stockFeign;

    @GlobalTransactional
    public ResponseBean buy(Integer pid, Integer count, String username) {
        // 获取分布式事务的全局id
        String xid = RootContext.getXID();
        BusinessActionContext context = new BusinessActionContext();
        context.setXid(xid);
        boolean success = stockFeign.deductStock(context,pid, count);
        if (success) {
            success = orderFeign.createOrder(context, username, pid, count);
        }
        if (success) return ResponseBean.OK("下单成功");
        return ResponseBean.ERROR("下单失败");
    }


}

```
另外，远程调用的接口类，由于修改了调用方式，改为了继承的方式去实现，所以修改代码
```java
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
```
```
package com.hengxing.business.feign;

import com.hengxing.common.api.StockApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 库存接口远程调用
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/11/2023 20:57:09
 */
@FeignClient("stock")
public interface StockFeign extends StockApi {

}
```

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35698483/1702915533473-2d10a426-8d07-4460-b9f0-7f7c18507fc0.png#averageHue=%2323272c&clientId=u53d6573a-78df-4&from=paste&height=394&id=u5dd6d485&originHeight=493&originWidth=1764&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=190620&status=done&style=none&taskId=u8a405f92-d348-4091-b7f0-dc8731cc4ee&title=&width=1411.2)

