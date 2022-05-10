package com.study.reproduce.handler;

import com.study.reproduce.model.domain.Admin;
import com.study.reproduce.model.request.AdminLoginData;
import com.study.reproduce.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminHandler {

    @Resource
    AdminService adminService;
    @Resource
    BlogService blogService;
    @Resource
    CategoryService categoryService;
    @Resource
    CommentService commentService;
    @Resource
    TagService tagService;
    @Resource
    LinkService linkService;

    @PostMapping("/login")
    public String login(AdminLoginData loginData, HttpSession session) {
        String account = loginData.getAccount();
        String password = loginData.getPassword();
        String verifyCode = loginData.getVerifyCode();
        if (StringUtils.isAnyBlank(account, password, verifyCode)) {
            return null;
        }
        Admin admin = adminService.login(account, password, verifyCode, session);
        //TODO 包装返回信息
        return "redirect:/admin/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(AdminService.GET_ADMIN_KEY);
        return "admin/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("path", "/admin/index");
        model.addAttribute("blogCount", blogService.count());
        model.addAttribute("categoryCount", categoryService.count());
        model.addAttribute("commentCount", commentService.count());
        model.addAttribute("tagCount", tagService.count());
        model.addAttribute("linkCount", linkService.count());
        return "admin/index";
    }

    @GetMapping("/blogs")
    public String blogs(Model model) {
        model.addAttribute("path", "/admin/blogs");
        return "admin/blog";
    }

    @GetMapping("/comments")
    public String comments(Model model) {
        model.addAttribute("path", "admin/comments");
        return "admin/comment";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("path", "admin/categories");
        return "admin/category";
    }

    @GetMapping("/tags")
    public String tags(Model model) {
        model.addAttribute("path", "admin/tags");
        return "admin/tag";
    }

    @GetMapping("/links")
    public String links(Model model) {
        model.addAttribute("path", "admin/links");
        return "admin/link";
    }

    @GetMapping("/configurations")
    public String configurations(Model model) {
        model.addAttribute("path", "admin/links");
        return "admin/configuration";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        Object admin = session.getAttribute(AdminService.GET_ADMIN_KEY);
        if (admin != null) {
            Admin adminInfo = (Admin) admin;
            model.addAttribute("loginUserName", adminInfo.getLoginUserName());
            model.addAttribute("nickName", adminInfo.getNickName());
        }
        model.addAttribute("path", "admin/links");
        return "admin/profile";
    }
}
