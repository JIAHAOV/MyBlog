package com.study.reproduce.interceptor;


import cn.dev33.satoken.stp.StpUtil;
import com.study.reproduce.service.AdminService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getServletPath().startsWith("/admin") &&
                !StpUtil.isLogin()) {
            request.getSession().setAttribute("errorMsg", "请先登录");
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        } else {
            
            return true;
        }
    }
}
