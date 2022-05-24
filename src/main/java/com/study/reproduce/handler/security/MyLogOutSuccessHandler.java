package com.study.reproduce.handler.security;

import com.study.reproduce.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyLogOutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        request.getSession().removeAttribute(AdminService.GET_ADMIN_KEY);
        request.getSession().removeAttribute("errorMsg");
        response.sendRedirect(request.getContextPath() + "/admin/login");
    }
}
