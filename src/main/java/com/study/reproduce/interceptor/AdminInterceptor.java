package com.study.reproduce.interceptor;


import com.study.reproduce.service.AdminService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object admin = request.getSession().getAttribute(AdminService.GET_ADMIN_KEY);
        if (admin == null) {
            response.sendRedirect("/admin/login");
            return false;
        } else {
            return true;
        }
    }
}
