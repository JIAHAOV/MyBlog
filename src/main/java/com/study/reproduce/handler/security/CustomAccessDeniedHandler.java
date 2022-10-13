package com.study.reproduce.handler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.reproduce.exception.ExceptionGenerator;
import com.study.reproduce.utils.Result;
import com.study.reproduce.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 访问失败，调用该 handle
 */
@Slf4j
@Component("accessDeniedHandler")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Result<?> failResult = ResultGenerator.getFailResult("权限不足", 40002);
        request.getSession().setAttribute("errorMsg", "权限不足");
        log.info("没有权限");
        ObjectMapper mapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            mapper.writeValue(response.getWriter(), failResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
