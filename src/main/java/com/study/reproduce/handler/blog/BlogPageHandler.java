package com.study.reproduce.handler.blog;

import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.service.BlogService;
import com.study.reproduce.service.TagService;
import com.study.reproduce.service.WebSiteConfigService;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class BlogPageHandler {
    @Resource
    BlogService blogService;

    @Resource
    TagService tagService;

    @Resource
    WebSiteConfigService webSiteConfigService;


    @GetMapping({"/", "/index", "index.html"})
    public String index(HttpServletRequest request) {
        return this.page(request, 1);
    }

    /**
     * 首页 分页数据
     *
     * @return
     */
    @GetMapping({"/page/{pageNum}"})
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum) {
        PageResult<Blog> blogPageResult = blogService.queryByPageUtil(new PageQueryUtil(1, 3));
        if (blogPageResult == null) {
            return "error/404";
        }
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("newBlogs", blogService.queryByPageUtil(new PageQueryUtil(1, 3)));
        request.setAttribute("hotBlogs", blogService.queryByPageUtil(new PageQueryUtil(1, 3)));
        request.setAttribute("hotTags", tagService.queryByPageUtil(new PageQueryUtil(1, 3)));
        request.setAttribute("pageName", "首页");
        request.setAttribute("configurations", webSiteConfigService.getAllConfigs());
        return "blog/amaze/index";
    }
}
