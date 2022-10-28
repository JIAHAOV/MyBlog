package com.study.reproduce.handler.index;

import com.study.reproduce.common.Result;
import com.study.reproduce.confiig.WebSiteStyleConfig;
import com.study.reproduce.exception.ExceptionGenerator;
import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.model.domain.Comment;
import com.study.reproduce.model.domain.Link;
import com.study.reproduce.model.ov.BlogDetail;
import com.study.reproduce.model.ov.BlogForDisplay;
import com.study.reproduce.model.ov.BlogInfo;
import com.study.reproduce.service.*;
import com.study.reproduce.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.study.reproduce.constant.RedisConstant.VERIFY_CODE;

@Controller
public class BlogIndexHandler {
    @Resource
    LinkService linkService;

    @Resource
    BlogService blogService;

    @Resource
    CommentService commentService;

    @Resource
    CategoryService categoryService;

    @Resource
    WebSiteConfigService webSiteConfigService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    BlogTagRelationService blogTagRelationService;


    @GetMapping({"/", "/index", "index.html"})
    public String index(Model model) {
        return this.page(model, 1);
    }

    /**
     * 首页 分页数据
     *
     * @return
     */
    @GetMapping({"/page/{pageNum}"})
    public String page(Model model, @PathVariable("pageNum") int pageNum) {
        PageResult<BlogInfo> pageResult = blogService.queryBlogInfoByPage(pageNum, 9);
//        PageResult<BlogForDisplay> blogPageResult = blogService.getBlogsForIndexPage(pageNum);

        model.addAttribute("blogPageResult", pageResult);
        model.addAttribute("pageName", "首页");
        setIndexMessage(model);
        return "blog/" + WebSiteStyleConfig.style + "/index";
    }

    @GetMapping({"/search/{keyword}"})
    public String search(Model model, @PathVariable("keyword") String keyword) {
        return search(model, keyword, 1);
    }

    @GetMapping({"/search/{keyword}/{page}"})
    public String search(Model model, @PathVariable("keyword") String keyword, @PathVariable("page") Integer page) {
        PageQueryUtil queryUtil = new PageQueryUtil(keyword, page);
        PageResult<BlogInfo> blogPageResult = blogService.search(page, keyword);
        model.addAttribute("blogPageResult", blogPageResult);
        model.addAttribute("pageName", "搜索");
        model.addAttribute("pageUrl", "search");
        model.addAttribute("keyword", keyword);
        setIndexMessage(model);
        return "blog/" + WebSiteStyleConfig.style + "/list";
    }

    @GetMapping({"/blog/{blogId}", "/article/{blogId}"})
    public String detail(HttpServletRequest request,
                         @PathVariable("blogId") Long blogId,
                         @RequestParam(value = "commentPage", defaultValue = "1") Integer commentPage) {
        BlogDetail blogDetail = blogService.getBlogDetailFromCache(blogId);
        if (blogDetail != null) {
            request.setAttribute("blogDetailVO", blogDetail);
//            request.setAttribute("commentPageResult", commentService.getCommentPage(blogId, commentPage));
            request.setAttribute("commentPageResult", commentService.getCommentPage(blogId, commentPage));
        }
        request.setAttribute("pageName", "详情");
        request.setAttribute("configurations", webSiteConfigService.getAllConfigs());
        return "blog/" + WebSiteStyleConfig.style + "/detail";
    }

    @GetMapping({"/categories"})
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.list());
        model.addAttribute("pageName", "分类页面");
        setIndexMessage(model);
        return "blog/" + WebSiteStyleConfig.style + "/category";
    }

    @GetMapping({"/tag/{tagName}"})
    public String tag(Model model, @PathVariable("tagName") String tagName) {
        return tag(model, tagName, 1);
    }

    /**
     * 根据标签查看文章
     * @param model
     * @param tagName
     * @param page
     * @return
     */
    @GetMapping({"/tag/{tagName}/{page}"})
    public String tag(Model model, @PathVariable("tagName") String tagName, @PathVariable("page") Integer page) {
        PageResult<Blog> blogPageResult = blogService.getBlogsPageByTypeName(tagName, page, 0);
        model.addAttribute("blogPageResult", blogPageResult);
        model.addAttribute("pageName", "标签");
        model.addAttribute("pageUrl", "tag");
        model.addAttribute("keyword", tagName);
        setIndexMessage(model);
        return "blog/" + WebSiteStyleConfig.style + "/list";
    }

    @GetMapping({"/category/{categoryName}"})
    public String category(Model model, @PathVariable("categoryName") String categoryName) {
        return category(model, categoryName, 1);
    }

    /**
     * 分类列表页
     *
     * @return
     */
    @GetMapping({"/category/{categoryName}/{page}"})
    public String category(Model model, @PathVariable("categoryName") String categoryName, @PathVariable("page") Integer page) {
        PageResult<Blog> blogPageResult = blogService.getBlogsPageByTypeName(categoryName, page, 1);
        model.addAttribute("blogPageResult", blogPageResult);
        model.addAttribute("pageName", "分类");
        model.addAttribute("pageUrl", "category");
        model.addAttribute("keyword", categoryName);
        setIndexMessage(model);
        return "blog/" + WebSiteStyleConfig.style + "/list";
    }

    /**
     * 提交评论
     * @param request
     * @param verifyCode
     * @param comment
     * @return
     */
    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public Result comment(HttpServletRequest request, String verifyCode, Comment comment) {
        if (StringUtils.isBlank(verifyCode)) {
            throw ExceptionGenerator.businessError("验证码不能为空");
        }
        String captchaCode = stringRedisTemplate.opsForValue().get(VERIFY_CODE + request.getSession().getId());
        String ref = request.getHeader("Referer");
        if (StringUtils.isAnyBlank(captchaCode, ref)) {
            throw ExceptionGenerator.businessError("非法请求");
        }
        if (!verifyCode.equals(captchaCode)) {
            throw ExceptionGenerator.businessError("验证码错误");
        }
        if (comment.getBlogId() == null || comment.getBlogId() < 0) {
            throw ExceptionGenerator.businessError("非法请求");
        }
        if (StringUtils.isBlank(comment.getCommentator())) {
            throw ExceptionGenerator.businessError("请输入称呼");
        }
        if (StringUtils.isBlank(comment.getEmail())) {
            throw ExceptionGenerator.businessError("请输入邮箱地址");
        }
        if (!PatternUtil.isEmail(comment.getEmail())) {
            throw ExceptionGenerator.businessError("请输入正确的邮箱地址");
        }
        if (!StringUtils.isBlank(comment.getWebsiteUrl()) && !PatternUtil.isURL(comment.getWebsiteUrl())) {
            throw ExceptionGenerator.businessError("请输入正确的网址");
        }
        if (StringUtils.isBlank(comment.getCommentBody())) {
            throw ExceptionGenerator.businessError("请输入评论内容");
        }
        if (comment.getCommentBody().trim().length() > 200) {
            throw ExceptionGenerator.businessError("评论内容过长");
        }
        boolean result = commentService.save(comment);
        return ResultGenerator.getSuccessResult(result);
    }

    @GetMapping({"/link"})
    public String link(HttpServletRequest request) {
        request.setAttribute("pageName", "友情链接");
        Map<Integer, List<Link>> linkMap = linkService.getLinksForLinkPage();
        if (linkMap != null) {
            //判断友链类别并封装数据 0-友链 1-推荐 2-个人网站
            if (linkMap.containsKey(0)) {
                request.setAttribute("favoriteLinks", linkMap.get(0));
            }
            if (linkMap.containsKey(1)) {
                request.setAttribute("recommendLinks", linkMap.get(1));
            }
            if (linkMap.containsKey(2)) {
                request.setAttribute("personalLinks", linkMap.get(2));
            }
        }
        request.setAttribute("configurations", webSiteConfigService.getAllConfigs());
        return "blog/" + WebSiteStyleConfig.style + "/link";
    }

    @GetMapping({"/{subUrl}"})
    public String detail(HttpServletRequest request, @PathVariable("subUrl") String subUrl) {
        BlogForDisplay blogDetail = blogService.getBlogDetailBySubUrl(subUrl);
        if (blogDetail != null) {
            request.setAttribute("blogDetailVO", blogDetail);
            request.setAttribute("pageName", subUrl);
            request.setAttribute("configurations", webSiteConfigService.getAllConfigs());
            return "blog/" + WebSiteStyleConfig.style + "/detail";
        } else {
            return "error/400";
        }
    }

    /**
     * 设置页面数据
     * @param model
     */
    public void setIndexMessage(Model model) {
//        model.addAttribute("newBlogs", blogService.getSimpleBlogInfoIndex(1));
        model.addAttribute("newBlogs", blogService.getNewBlogsFromCache());
//        model.addAttribute("hotBlogs", blogService.getSimpleBlogInfoIndex(0));
        model.addAttribute("hotBlogs", blogService.getHotBlogs());
//        model.addAttribute("hotTags", blogTagRelationService.getBlogTagCountIndex());
        model.addAttribute("hotTags", blogTagRelationService.getTagCount());
//        model.addAttribute("configurations", webSiteConfigService.getAllConfigs());
        model.addAttribute("configurations", webSiteConfigService.getAllConfigs());
    }
}
