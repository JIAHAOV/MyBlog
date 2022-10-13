package com.study.reproduce.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class LogInterceptor {
    /**
     * 打印请求响应日志
     */
    @Around("execution(* com.study.reproduce.handler.*.*.*(..))")
    public Object logInterceptor(ProceedingJoinPoint point) throws Throwable {
        //计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //获取请求路径
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String uri = request.getRequestURI();
        String ip = request.getRemoteHost();
        String requestId = UUID.randomUUID().toString();
        //获取请求参数
        Object[] args = point.getArgs();
        String params = "[" + StringUtils.joinWith(",", args) + "]";
        log.info("request start，id: {}, path: {}, ip: {}, params: {}", requestId, uri, ip, params);
        //执行
        Object result = point.proceed();
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("request end, id: {}, cost: {}ms", requestId, totalTimeMillis);
        return result;
    }
}
