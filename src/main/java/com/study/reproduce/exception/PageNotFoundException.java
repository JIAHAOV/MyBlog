package com.study.reproduce.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageNotFoundException extends RuntimeException{
    private String message;
    private Integer errorCode;

    public PageNotFoundException(String message) {
        this.message = message;
    }
}
