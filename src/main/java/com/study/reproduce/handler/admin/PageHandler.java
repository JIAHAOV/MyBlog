package com.study.reproduce.handler.admin;

import com.study.reproduce.model.domain.Admin;
import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.removeAttribute(AdminService.GET_ADMIN_KEY);
//        session.removeAttribute("errorMsg");
//        return "admin/login";
//    }

    @GetMapping(value = {"/login"})
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping(value = {"/index", "", "/"})
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Admin admin = adminService.getAdminByUsername(user.getUsername());
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

    @GetMapping("/blogs/edit/{blogId}")
    public String blogEdit(Model model, @PathVariable String blogId) {
        Blog blog = blogService.getById(blogId);
        if (blog == null) {
            return "error/404";
        }
        model.addAttribute("blog", blog);
        model.addAttribute("path", "admin/blogs/edit");
        model.addAttribute("categories", categoryService.list());
        return "admin/edit";
    }

    @GetMapping("/blogs/edit")
    public String edit(Model model) {
        model.addAttribute("path", "edit");
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
