package com.study.reproduce.filter;

import com.study.reproduce.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class SecurityLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public SecurityLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/login","POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //在这里获取前端传入的数据
        String verifyCode = request.getParameter("verifyCode");
        String defaultVerifyCode = (String) request.getSession().getAttribute("verifyCode");
        if (!defaultVerifyCode.equalsIgnoreCase(verifyCode)) {
            throw new AuthenticationServiceException("验证码输入错误");
        }
        String username = request.getParameter("account");
        String password = request.getParameter("password");
        username = (username != null) ? username : "";
        username = username.trim();
        password = (password != null) ? password : "";

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());

        return authenticationManager.authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        log.info(user.getUsername() + "  登录");
        request.getSession().setAttribute(AdminService.GET_ADMIN_KEY, user.getUsername());
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        request.getSession().setAttribute("errorMsg", failed.getMessage());
        response.sendRedirect(request.getContextPath() + "/admin/login");
    }
}
