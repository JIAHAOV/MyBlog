package com.study.reproduce.handler.admin;

import com.study.reproduce.exception.ExceptionManager;
import com.study.reproduce.model.domain.Tag;
import com.study.reproduce.model.request.PageParam;
import com.study.reproduce.service.TagService;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;
import com.study.reproduce.utils.Result;
import com.study.reproduce.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/admin")
public class TagHandler {

    @Resource
    TagService tagService;

    @GetMapping("/tags/list")
    public Result list(PageParam pageParam) {
        if (pageParam.getPage() == null || pageParam.getLimit() == null) {
            throw ExceptionManager.genException("参数错误");
        }
        PageQueryUtil queryUtil = new PageQueryUtil(pageParam);
        PageResult<Tag> pageResult = tagService.queryByPageUtil(queryUtil);
        return ResultGenerator.getSuccessResult(pageResult);
    }

    @PostMapping("/tags/save")
    public Result save(String tagName) {
        if (tagName.isEmpty()) {
            throw ExceptionManager.genException("参数错误");
        }
        if (tagService.saveTag(tagName)) {
            return ResultGenerator.getSuccessResult("新增成功");
        } else {
            throw ExceptionManager.genException("新增失败");
        }
    }

    @PostMapping("/tags/delete")
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length == 0) {
            throw ExceptionManager.genException("参数错误");
        }
        if (tagService.removeBatchTags(ids)) {
            return ResultGenerator.getSuccessResult("删除成功");
        } else {
            throw ExceptionManager.genException("删除失败");
        }
    }
}
