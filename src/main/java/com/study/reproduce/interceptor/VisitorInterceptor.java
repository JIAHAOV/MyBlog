package com.study.reproduce.interceptor;


import com.study.reproduce.model.ov.Visitor;
import com.study.reproduce.utils.VisitorHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.StringTokenizer;

import static com.study.reproduce.utils.IpUtils.isIPv4Private;
import static com.study.reproduce.utils.IpUtils.isIPv4Valid;

@Slf4j
public class VisitorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = getIpFromRequest(request);
        Visitor visitor = new Visitor(ip);
        VisitorHolder.saveVisitor(visitor);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        VisitorHolder.removeVisitor();
    }

    public String getIpFromRequest(HttpServletRequest request) {
        String ip;

        if (StringUtils.isNotBlank(ip = request.getHeader("x-real-ip"))) {
            return ip;
        }
        boolean found = false;
        if (StringUtils.isNotBlank((ip = request.getHeader("x-forwarded-for")))) {
            StringTokenizer tokenizer = new StringTokenizer(ip, ",");
            while (tokenizer.hasMoreTokens()) {
                ip = tokenizer.nextToken().trim();
                if (isIPv4Valid(ip) && !isIPv4Private(ip)) {
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.equals(ip, "0:0:0:0:0:0:0:1")) {
            log.error("未能解析IP, 可能原因[nginx未配置,传递真实IP]");
        }
        return ip;
    }
}
