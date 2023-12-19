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

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", money=" + money +
                ", freezeMoney=" + freezeMoney +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!Objects.equals(id, account.id)) return false;
        if (!Objects.equals(username, account.username)) return false;
        if (!Objects.equals(money, account.money)) return false;
        return Objects.equals(freezeMoney, account.freezeMoney);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (money != null ? money.hashCode() : 0);
        result = 31 * result + (freezeMoney != null ? freezeMoney.hashCode() : 0);
        return result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Account() {
    }

    public Account(Integer id, String username, Double money) {
        this.id = id;
        this.username = username;
        this.money = money;
    }

    public Double getFreezeMoney() {
        return freezeMoney;
    }

    public void setFreezeMoney(Double freezeMoney) {
        this.freezeMoney = freezeMoney;
    }
}
