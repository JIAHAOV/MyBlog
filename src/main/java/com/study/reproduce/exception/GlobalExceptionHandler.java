package com.study.reproduce.exception;

import com.study.reproduce.utils.Result;
import com.study.reproduce.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result customException(BusinessException e, HttpSession session) {
        Result failResult = ResultGenerator.getFailResult(e.getMessage(), e.getErrorCode());
        session.setAttribute("errorMsg", e.getMessage());
        return failResult;
    }

    @ExceptionHandler(PageNotFoundException.class)
    public String pageNotFoundException(RuntimeException e) {
        log.warn(e.getStackTrace()[0].toString() + " : PageNotFoundException");
        return "error/404";
    }
}
