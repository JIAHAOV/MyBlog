package com.study.reproduce.utils;

/**
 * 快速获取 Result
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    private static final int RESULT_CODE_SUCCESS = 200;
    private static final int RESULT_CODE_SERVER_ERROR = 500;

    /**
     * 获取带数据的 Result
     * @param data 数据
     * @param <T> 数据类型
     * @return Result
     */
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

    public static Result getFailResult(String message, Integer resultCode) {
        Result result = new Result();
        result.setMessage(message);
        if (resultCode == null) {
            result.setResultCode(RESULT_CODE_SERVER_ERROR);
        } else {
            result.setResultCode(resultCode);
        }
        return result;
    }
}
