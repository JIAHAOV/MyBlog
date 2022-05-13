package com.study.reproduce.handler;

import com.study.reproduce.model.domain.Admin;
import com.study.reproduce.model.request.AdminLoginData;
import com.study.reproduce.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    @Resource
    WebSiteConfigService webSiteConfigService;

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

    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request, String originalPassword, String newPassword) {
        if (StringUtils.isAnyBlank(originalPassword, newPassword)) {
            return "参数不能为空";
        }
        Integer adminUserId = getAdminUserId(request);
        if (adminUserId == null) {
            return "请先登录";
        }
        if (adminService.updatePassword(adminUserId, originalPassword, newPassword)) {
            //清除之前的登录信息
            request.getSession().removeAttribute(AdminService.GET_ADMIN_KEY);
            request.getSession().removeAttribute("errorMsg");
            //返回 success 让前端重新登录
            return "success";
        }
        return "修改失败";
    }

    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request, String loginUserName, String nickName) {
        if (StringUtils.isAnyBlank(loginUserName, nickName)) {
            return "参数不能为空";
        }
        Integer adminUserId = getAdminUserId(request);
        if (adminUserId == null) {
            return "请先登录";
        }
        if (adminService.updateName(adminUserId, loginUserName, nickName)) {
            return "success";
        } else {
            return "修改失败";
        }
    }

    /**
     * 获取登录管理员id
     * @param request request
     * @return id
     */
    public Integer getAdminUserId(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(AdminService.GET_ADMIN_KEY);
        if (attribute == null) {
            return null;
        }
        Admin admin = (Admin)attribute;
        return admin.getAdminUserId();
    }
}
