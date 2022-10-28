package com.study.reproduce.exception;

import cn.dev33.satoken.exception.NotRoleException;
import com.study.reproduce.common.Result;
import com.study.reproduce.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<?> customException(BusinessException e, HttpSession session) {
        Result<?> failResult = ResultGenerator.getFailResult(e.getMessage(), e.getErrorCode());
        session.setAttribute("errorMsg", e.getMessage());
        e.printStackTrace();
        return failResult;
    }

    @ExceptionHandler(NotRoleException.class)
    public Result<?> notRoleException(NotRoleException e, HttpSession session) {
        Result<?> failResult = ResultGenerator.getFailResult("没有权限");
        session.setAttribute("errorMsg", "没有权限");
        return failResult;
    }
}
