package com.study.reproduce.handler.admin;

import com.study.reproduce.exception.ExceptionGenerator;
import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.model.request.PageParam;
import com.study.reproduce.service.BlogService;
import com.study.reproduce.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j

@RestController
@RequestMapping("/admin")
public class BlogHandler {
    @Resource
    BlogService blogService;

    @GetMapping("/blogs/list")
    public Result list(PageParam pageParam) {
        if (pageParam.getPage() == null || pageParam.getLimit() == null) {
            throw ExceptionGenerator.businessError("参数错误");
        }
        PageQueryUtil queryUtil = new PageQueryUtil(pageParam);
        PageResult<Blog> pageResult = blogService.queryByPageUtil(queryUtil);
        return ResultGenerator.getSuccessResult(pageResult);
    }

    @PostMapping("/blogs/save")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public Result save(Blog blog) {
        checkBlogInfo(blog);
        if (blogService.saveBlog(blog)) {
            return ResultGenerator.getSuccessResult("添加成功");
        } else {
            throw ExceptionGenerator.businessError("新增失败");
        }
    }

    @PostMapping("/blogs/update")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public Result update(Blog blog) {
        checkBlogInfo(blog);
        if (blogService.updateBlog(blog)) {
            return ResultGenerator.getSuccessResult("更新成功");
        } else {
            throw ExceptionGenerator.businessError("更新失败");
        }
    }

    @PostMapping("/blogs/delete")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public Result delete(@RequestBody List<Integer> ids) {
        if (ids == null || ids.size() <= 0) {
            throw ExceptionGenerator.businessError("参数异常");
        }
        boolean result = blogService.deleteBlogs(ids);
        if (result) {
            return ResultGenerator.getSuccessResult("删除成功");
        } else {
            throw ExceptionGenerator.businessError("删除失败");
        }
    }

    @PostMapping("/blogs/md/uploadfile")
    public void uploadFileByEditorMd(@RequestParam(name = "editormd-image-file") MultipartFile multipartFile,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws IOException {
        File file = FileUtil.getUploadFile(BlogHandler.class, multipartFile);
        System.out.println(file.getAbsolutePath());
        try {
            //上传文件
            multipartFile.transferTo(file);
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");
            String url = request.getRequestURL().toString();
            //回复
            URI uri = URIUtil.getResponseURI(new URI(url), file.getName());
            response.getWriter().write("{\"success\": 1, \"message\":\"success\",\"url\":\"" + uri + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":0}");
            throw new RuntimeException("上传错误");
        }
    }

    public void checkBlogInfo(Blog blog) {
        if (blog.getBlogTitle().isEmpty()) {
            throw ExceptionGenerator.businessError("标题不能为空");
        }
        if (blog.getBlogTitle().length() > 150) {
            throw ExceptionGenerator.businessError("标题过长");
        }
        if (blog.getBlogTags().isEmpty()) {
            throw ExceptionGenerator.businessError("标签不能为空");
        }
        if (blog.getBlogTags().length() > 150) {
            throw ExceptionGenerator.businessError("标签过长");
        }
        if (blog.getBlogSubUrl().length() > 150) {
            throw ExceptionGenerator.businessError("url路径过长");
        }
        if (blog.getBlogContent().isEmpty()) {
            throw ExceptionGenerator.businessError("请输入文章内容");
        }
        if (blog.getBlogContent().trim().length() > 50000) {
            throw ExceptionGenerator.businessError("文章内容过长");
        }
        if (blog.getBlogCoverImage().isEmpty()) {
            throw ExceptionGenerator.businessError("封面不能为空");
        }
    }
}
