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

    public Stock(Integer id, Integer pid, Integer count, Integer freezeCount) {
        this.id = id;
        this.pid = pid;
        this.count = count;
        this.freezeCount = freezeCount;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", pid=" + pid +
                ", count=" + count +
                ", freezeCount=" + freezeCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stock stock = (Stock) o;

        if (!Objects.equals(id, stock.id)) return false;
        if (!Objects.equals(pid, stock.pid)) return false;
        if (!Objects.equals(count, stock.count)) return false;
        return Objects.equals(freezeCount, stock.freezeCount);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (pid != null ? pid.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (freezeCount != null ? freezeCount.hashCode() : 0);
        return result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Stock() {
    }

    public Stock(Integer id, Integer pid, Integer count) {
        this.id = id;
        this.pid = pid;
        this.count = count;
    }

    public Integer getFreezeCount() {
        return freezeCount;
    }

    public void setFreezeCount(Integer freezeCount) {
        this.freezeCount = freezeCount;
    }
}
