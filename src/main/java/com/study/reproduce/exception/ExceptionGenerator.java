package com.study.reproduce.exception;

import org.apache.commons.lang3.StringUtils;

public class ExceptionGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    private static final int RESULT_CODE_SUCCESS = 200;
    private static final int RESULT_CODE_SERVER_ERROR = 500;

    public static BusinessException businessError(String message) {
        BusinessException businessException = new BusinessException();
        businessException.setErrorCode(RESULT_CODE_SERVER_ERROR);
        if (StringUtils.isEmpty(message)) {
            businessException.setMessage(DEFAULT_FAIL_MESSAGE);
        } else {
            businessException.setMessage(message);
        }
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
