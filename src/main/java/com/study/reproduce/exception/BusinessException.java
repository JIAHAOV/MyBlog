package com.study.reproduce.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException{
    private String message;
    private Integer errorCode;

    public BusinessException(String message) {
        this.message = message;
    }
}
