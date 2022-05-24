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
        e.printStackTrace();
        return "error/404";
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public Result customException(AccessDeniedException e, HttpSession session) {
//        Result failResult = ResultGenerator.getFailResult("删除失败", 500);
//        session.setAttribute("errorMsg", e.getMessage());
//        return failResult;
//    }

//    @ExceptionHandler(RuntimeException.class)
//    public String runtimeException(RuntimeException e, HttpServletResponse response, HttpServletRequest request) throws IOException {
//        request.getSession().setAttribute("errorMsg", e.getMessage());
//        HttpStatus status = getStatus(request);
//        e.printStackTrace();
//        if (status == HttpStatus.BAD_REQUEST) {
//            return "/error/400";
//        } else if (status == HttpStatus.NOT_FOUND) {
//            return "/error/404";
//        } else {
//            return "/error/5xx";
//        }
//    }
//
//    public HttpStatus getStatus(HttpServletRequest request) {
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        if (statusCode == null) {
//            return HttpStatus.INTERNAL_SERVER_ERROR;
//        } else {
//            return HttpStatus.valueOf(statusCode);
//        }
//    }

}
