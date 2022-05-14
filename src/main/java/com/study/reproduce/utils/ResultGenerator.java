package com.study.reproduce.utils;

public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    private static final int RESULT_CODE_SUCCESS = 200;
    private static final int RESULT_CODE_SERVER_ERROR = 500;

    public static <T> Result<T> getSuccessResult(T data) {
        Result<T> result = new Result<T>();
        result.setData(data);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        result.setResultCode(RESULT_CODE_SUCCESS);
        return result;
    }


    public static Result getSuccessResult() {
        Result result = new Result();
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        result.setResultCode(RESULT_CODE_SUCCESS);
        return result;
    }

    public static Result getSuccessResult(String message) {
        Result result = new Result();
        result.setMessage(message);
        result.setResultCode(RESULT_CODE_SUCCESS);
        return result;
    }

    public static Result getFailResult(String message) {
        Result result = new Result();
        result.setMessage(message);
        result.setResultCode(40001);
        return result;
    }
}
