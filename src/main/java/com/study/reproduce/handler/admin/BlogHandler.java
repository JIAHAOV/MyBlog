package com.study.reproduce.handler.admin;

import com.study.reproduce.common.Result;
import com.study.reproduce.constant.BlogConstant;
import com.study.reproduce.exception.ExceptionGenerator;
import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.model.ov.BlogInfo;
import com.study.reproduce.model.request.PageParam;
import com.study.reproduce.service.BlogService;
import com.study.reproduce.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
public class BlogHandler {

    @Resource
    BlogService blogService;

    /**
     * 分页获取文章简略信息
     */
    @GetMapping("/blogs/list")
    public Result<PageResult<Blog>> list(PageParam pageParam) {
        if (pageParam.getPage() == null || pageParam.getLimit() == null) {
            throw ExceptionGenerator.businessError("参数错误");
        }
        PageQueryUtil queryUtil = new PageQueryUtil(pageParam);
        PageResult<Blog> pageResult = blogService.queryByPageUtil(queryUtil);
        return ResultGenerator.getSuccessResult(pageResult);
    }

    /**
     * 新增
     */
    @PostMapping("/blogs/save")
    public Result<?> save(Blog blog) {
        checkBlogInfo(blog);
        if (blogService.saveBlog(blog)) {
            return ResultGenerator.getSuccessResult("添加成功");
        } else {
            throw ExceptionGenerator.businessError("新增失败");
        }
    }

    /**
     * 更新
     */
    @PostMapping("/blogs/update")
    public Result<?> update(Blog blog) {
        checkBlogInfo(blog);
        if (blogService.updateBlog(blog)) {
            return ResultGenerator.getSuccessResult("更新成功");
        } else {
            throw ExceptionGenerator.businessError("更新失败");
        }
    }

    /**
     * 删除
     */
    @PostMapping("/blogs/delete")
    public Result<?> delete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw ExceptionGenerator.businessError("参数异常");
        }
        boolean result = blogService.deleteBlogs(ids);
        if (result) {
            return ResultGenerator.getSuccessResult("删除成功");
        } else {
            throw ExceptionGenerator.businessError("删除失败");
        }
    }

    /**
     * 检查文章内容是否合法
     */
    public void checkBlogInfo(Blog blog) {
        if (blog.getBlogTitle().isEmpty()) {
            throw ExceptionGenerator.businessError("标题不能为空");
        }
        if (blog.getBlogTitle().length() > BlogConstant.maxTitleLength) {
            throw ExceptionGenerator.businessError("标题过长");
        }
        if (blog.getBlogTags().isEmpty()) {
            throw ExceptionGenerator.businessError("标签不能为空");
        }
        if (blog.getBlogTags().length() > BlogConstant.maxTagsLength) {
            throw ExceptionGenerator.businessError("标签过长");
        }
        if (blog.getBlogSubUrl().length() > BlogConstant.maxUrlLength) {
            throw ExceptionGenerator.businessError("url路径过长");
        }
        if (blog.getBlogContent().isEmpty()) {
            throw ExceptionGenerator.businessError("请输入文章内容");
        }
        if (blog.getBlogContent().trim().length() > BlogConstant.maxContentLength) {
            throw ExceptionGenerator.businessError("文章内容过长");
        }
        if (blog.getBlogCoverImage().isEmpty()) {
            throw ExceptionGenerator.businessError("封面不能为空");
        }
    }
}
