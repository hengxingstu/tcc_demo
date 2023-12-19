package com.hengxing.common.exception;

import com.hengxing.common.response.ResponseBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author hengxing
 * @version 1.0
 * @project at_demo
 * @date 12/13/2023 11:17:39
 */
@RestControllerAdvice
public class GlobalException {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseBean runtimeException(Exception e){
        return ResponseBean.ERROR(e.getMessage());
    }
}
