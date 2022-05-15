package com.study.reproduce.handler.admin;

import com.study.reproduce.exception.ExceptionManager;
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
import java.util.List;

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
            throw ExceptionManager.genException("参数错误");
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
        checkBlogInfo(blog);
        if (blogService.saveBlog(blog)) {
            return ResultGenerator.getSuccessResult("新增成功");
        } else {
            throw ExceptionManager.genException("新增失败");
        }
    }

    @PostMapping("/blogs/update")
    public Result update(Blog blog) {
        checkBlogInfo(blog);
        if (blogService.updateBlog(blog)) {
            return ResultGenerator.getSuccessResult("更新成功");
        } else {
            throw ExceptionManager.genException("更新失败");
        }
    }

    @PostMapping("/blogs/delete")
    public Result delete(@RequestBody List<Integer> ids) {
        if (ids == null || ids.size() <= 0) {
            throw ExceptionManager.genException("参数异常");
        }
        boolean result = blogService.deleteBlogs(ids);
        if (result) {
            return ResultGenerator.getSuccessResult("删除成功");
        } else {
            throw ExceptionManager.genException("删除失败");
        }
    }

    public void checkBlogInfo(Blog blog) {
        if (blog.getBlogTitle().isEmpty()) {
            throw ExceptionManager.genException("标题不能为空");
        }
        if (blog.getBlogTitle().length() > 150) {
            throw ExceptionManager.genException("标题过长");
        }
        if (blog.getBlogTags().isEmpty()) {
            throw ExceptionManager.genException("标签不能为空");
        }
        if (blog.getBlogTags().length() > 150) {
            throw ExceptionManager.genException("标签过长");
        }
        if (blog.getBlogSubUrl().length() > 150) {
            throw ExceptionManager.genException("url路径过长");
        }
        if (blog.getBlogContent().isEmpty()) {
            throw ExceptionManager.genException("请输入文章内容");
        }
        if (blog.getBlogContent().length() > 50000) {
            throw ExceptionManager.genException("文章内容过长");
        }
        if (blog.getBlogCoverImage().isEmpty()) {
            throw ExceptionManager.genException("封面不能为空");
        }
    }
}
