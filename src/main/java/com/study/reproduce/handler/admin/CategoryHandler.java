package com.study.reproduce.handler.admin;

import com.study.reproduce.exception.ExceptionGenerator;
import com.study.reproduce.model.domain.Category;
import com.study.reproduce.model.request.PageParam;
import com.study.reproduce.service.CategoryService;
import com.study.reproduce.utils.PageResult;
import com.study.reproduce.utils.Result;
import com.study.reproduce.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
public class CategoryHandler {

    @Resource
    CategoryService categoryService;

    @GetMapping("/categories/list")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public Result list(PageParam pageParam) {
        if (pageParam == null && pageParam.getLimit() == null) {
            throw ExceptionGenerator.businessError("参数错误");
        }
        PageResult<Category> pageResult = categoryService.queryCategoryPage(pageParam.getPage(), pageParam.getLimit());
        return ResultGenerator.getSuccessResult(pageResult);
    }

    /**
     * 分类添加
     */
    @PostMapping("/categories/save")
    @PreAuthorize("hasAnyRole('admin')")
    public Result save(Category category) {
        checkCategoryInfo(category);
        boolean result = categoryService.saveCategory(category);
        log.info("新增: " + result + " : " + category);
        if (result) {
            return ResultGenerator.getSuccessResult();
        } else {
            throw ExceptionGenerator.businessError("新增失败");
        }
    }


    /**
     * 分类修改
     */
    @PostMapping("/categories/update")
    @PreAuthorize("hasAnyRole('admin')")
    public Result update(Category category) {
        checkCategoryInfo(category);
        boolean result = categoryService.updateById(category);
        log.info("修改: " + result + " : " + category);
        if (result) {
            return ResultGenerator.getSuccessResult();
        } else {
            throw ExceptionGenerator.businessError("修改失败");
        }
    }


    /**
     * 分类删除
     */
    @PostMapping("/categories/delete")
    @PreAuthorize("hasAnyRole('admin')")
    public Result delete(@RequestBody List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return ResultGenerator.getFailResult("参数错误");
        }
        boolean result = categoryService.deleteByIds(ids);
        if (result) {
            return ResultGenerator.getSuccessResult();
        } else {
            throw ExceptionGenerator.businessError("删除失败");
        }
    }

    /**
     * 检查是否符合规范
     */
    public void checkCategoryInfo(Category category) {
        if (category == null) {
            throw ExceptionGenerator.businessError("参数错误");
        }
        if (category.getCategoryIcon().isEmpty()) {
            throw ExceptionGenerator.businessError("请选择分类图标");
        }
        if (category.getCategoryName().isEmpty()) {
            throw ExceptionGenerator.businessError("请输入名称");
        }
    }
}
