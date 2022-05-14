package com.study.reproduce.handler;

import com.study.reproduce.model.domain.Admin;
import com.study.reproduce.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class PageHandler {
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
    @Resource
    WebSiteConfigService webSiteConfigService;

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(AdminService.GET_ADMIN_KEY);
        return "admin/login";
    }

    @GetMapping(value = {"/login", "/"})
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

    @GetMapping("/configurations")
    public String configurations(HttpSession session, Model model) {
        model.addAttribute("path", "admin/links");
        webSiteConfigService.setConfigMessage(session, model);
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

    @GetMapping("/blogs")
    public String blogs(Model model) {
        model.addAttribute("path", "/admin/blogs");
        return "admin/blog";
    }

    @GetMapping("/blogs/edit")
    public String blogEdit(Model model) {
        model.addAttribute("path", "admin/blogs/edit");
        model.addAttribute("categories", categoryService.list());
        return "admin/edit";
    }

    @GetMapping("/links")
    public String links(Model model) {
        model.addAttribute("path", "admin/links");
        return "admin/link";
    }

    @GetMapping("/tags")
    public String tags(Model model) {
        model.addAttribute("path", "admin/tags");
        return "admin/tag";
    }
}
