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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseBean that = (ResponseBean) o;

        if (!Objects.equals(status, that.status)) return false;
        if (!Objects.equals(message, that.message)) return false;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseBean() {
    }

    public ResponseBean(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    private ResponseBean(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
