package com.study.reproduce.handler;

import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.model.request.PageParam;
import com.study.reproduce.service.BlogService;
import com.study.reproduce.service.CategoryService;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;
import com.study.reproduce.utils.Result;
import com.study.reproduce.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/admin")
public class BlogHandler {
    @Resource
    BlogService blogService;
    @Resource
    CategoryService categoryService;



    @GetMapping("/blogs/list")
    public Result list(PageParam pageParam) {
        if (pageParam.getPage() == null || pageParam.getLimit() == null) {
            return ResultGenerator.getFailResult("参数错误");
        }
        PageQueryUtil queryUtil = new PageQueryUtil(pageParam);
        PageResult<Blog> pageResult = blogService.queryByPageUtil(queryUtil);
        return ResultGenerator.getSuccessResult(pageResult);
    }

    @GetMapping("/blogs/edit/{blogId}")
    public String edit(Model model, @PathVariable("blogId") Long blogId) {
        model.addAttribute("path", "edit");
        Blog blog = blogService.getById(blogId);
        if (blog == null) {
            return "error/error_400";
        }
        model.addAttribute("blog", blog);
        model.addAttribute("categories", categoryService.list());
        return "admin/edit";
    }

    @PostMapping("/blogs/save")
    public Result save(Blog blog) {
        Result checkBlogInfo = checkBlogInfo(blog);
        if (checkBlogInfo != null) {
            return checkBlogInfo;
        }
        String result = blogService.saveBlog(blog);
        if ("操作成功".equals(result)) {
            return ResultGenerator.getSuccessResult("新增成功");
        } else {
            return ResultGenerator.getFailResult(result);
        }
    }

    @PostMapping("/blogs/update")
    public Result update(Blog blog) {
        Result checkBlogInfo = checkBlogInfo(blog);
        if (checkBlogInfo != null) {
            return checkBlogInfo;
        }
        String result = blogService.updateBlog(blog);
        if ("操作成功".equals(result)) {
            return ResultGenerator.getSuccessResult("更新成功");
        } else {
            return ResultGenerator.getFailResult(result);
        }
    }

    @PostMapping("/blogs/delete")
    public Result delete(@RequestBody Integer[] ids) {
        if (ids == null || ids.length <= 0) {
            return ResultGenerator.getFailResult("参数异常");
        }
        boolean result = blogService.removeByIds(Arrays.asList(ids));
        if (result) {
            return ResultGenerator.getSuccessResult("删除成功");
        } else {
            return ResultGenerator.getFailResult("删除失败");
        }
    }

    public Result checkBlogInfo(Blog blog) {
        if (blog.getBlogTitle().isEmpty()) {
            return ResultGenerator.getFailResult("标题不能为空");
        }
        if (blog.getBlogTitle().length() > 150) {
            return ResultGenerator.getFailResult("标题过长");
        }
        if (blog.getBlogTags().isEmpty()) {
            return ResultGenerator.getFailResult("标签不能为空");
        }
        if (blog.getBlogTags().length() > 150) {
            return ResultGenerator.getFailResult("标签过长");
        }
        if (blog.getBlogSubUrl().length() > 150) {
            return ResultGenerator.getFailResult("url路径过长");
        }
        if (blog.getBlogContent().isEmpty()) {
            return ResultGenerator.getFailResult("请输入文章内容");
        }
        if (blog.getBlogContent().length() > 50000) {
            return ResultGenerator.getFailResult("文章内容过长");
        }
        if (blog.getBlogCoverImage().isEmpty()) {
            return ResultGenerator.getFailResult("封面不能为空");
        }
        return null;
    }
}
