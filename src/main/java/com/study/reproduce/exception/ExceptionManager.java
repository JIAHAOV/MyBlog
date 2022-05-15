package com.study.reproduce.exception;

public class ExceptionManager {

    public static CustomException genException(String message) {
        return new CustomException(message);
    }

    public static CustomException genException(String message, int errorCode) {
        return new CustomException(message, errorCode);
    }
}
