package com.study.reproduce.handler.admin;

import com.study.reproduce.model.domain.Category;
import com.study.reproduce.model.request.PageParam;
import com.study.reproduce.service.CategoryService;
import com.study.reproduce.utils.PageResult;
import com.study.reproduce.utils.Result;
import com.study.reproduce.utils.ResultGenerator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class CategoryHandler {

    @Resource
    CategoryService categoryService;

    @GetMapping("/categories/list")
    public Result list(PageParam pageParam) {
        if (pageParam == null && pageParam.getLimit() == null) {
            return ResultGenerator.getFailResult("参数错误");
        }
        PageResult<Category> pageResult = categoryService.queryCategoryPage(pageParam.getPage(), pageParam.getLimit());
        return ResultGenerator.getSuccessResult(pageResult);
    }

    /**
     * 分类添加
     */
    @PostMapping("/categories/save")
    public Result save(Category category) {
        Result result = checkCategoryInfo(category);
        if (result != null) {
            return result;
        }
        if (categoryService.save(category)) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("名称重复");
        }
    }


    /**
     * 分类修改
     */
    @PostMapping("/categories/update")
    public Result update(Category category) {
        Result result = checkCategoryInfo(category);
        if (result != null) {
            return result;
        }
        if (categoryService.updateById(category)) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("名称重复");
        }
    }


    /**
     * 分类删除
     */
    @PostMapping("/categories/delete")
    public Result delete(@RequestBody List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return ResultGenerator.getFailResult("参数错误");
        }
        if (categoryService.deleteByIds(ids)) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("删除失败");
        }
    }

    /**
     * 检查是否符合规范
     */
    public Result checkCategoryInfo(Category category) {
        if (category == null) {
            return ResultGenerator.getFailResult("参数错误");
        }
        if (category.getCategoryIcon().isEmpty()) {
            return ResultGenerator.getFailResult("请选择分类图标");
        }
        if (category.getCategoryName().isEmpty()) {
            return ResultGenerator.getFailResult("请输入名称");
        }
        return null;
    }
}
