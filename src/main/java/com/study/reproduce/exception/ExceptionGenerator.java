package com.study.reproduce.exception;

public class ExceptionGenerator {

    public static BusinessException businessError(String message) {
        BusinessException businessException = new BusinessException();
        businessException.setMessage(message);
        return businessException;
    }

    public static BusinessException businessError(String message, int errorCode) {
        return new BusinessException(message, errorCode);
    }

    public static PageNotFoundException pageNotFound(String message) {
        PageNotFoundException pageNotFoundException = new PageNotFoundException();
        pageNotFoundException.setMessage(message);
        return pageNotFoundException;
    }
}
