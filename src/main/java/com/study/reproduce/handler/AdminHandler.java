package com.study.reproduce.handler;

import com.study.reproduce.model.domain.Admin;
import com.study.reproduce.model.request.AdminLoginData;
import com.study.reproduce.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller("/admin")
public class AdminHandler {

    @Resource
    AdminService adminService;

    @PostMapping("/login")
    public Admin login(AdminLoginData loginData, HttpSession session) {
        String account = loginData.getAccount();
        String password = loginData.getPassword();
        if (StringUtils.isAnyBlank(account, password)) {
            return null;
        }
        Admin admin = adminService.login(account, password, session);
        //TODO 包装返回信息
        return admin;
    }
}
