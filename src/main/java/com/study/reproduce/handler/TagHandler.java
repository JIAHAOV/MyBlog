package com.study.reproduce.handler;

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
            return ResultGenerator.getFailResult("参数错误");
        }
        PageQueryUtil queryUtil = new PageQueryUtil(pageParam);
        PageResult<Tag> pageResult = tagService.queryByPageUtil(queryUtil);
        return ResultGenerator.getSuccessResult(pageResult);
    }

    @PostMapping("/tags/save")
    public Result save(String tagName) {
        if (tagName.isEmpty()) {
            ResultGenerator.getFailResult("参数错误");
        }
        String result = tagService.saveTag(tagName);
        if ("新增成功".equals(result)) {
            return ResultGenerator.getSuccessResult("新增成功");
        } else {
            return ResultGenerator.getFailResult("新增失败");
        }
    }

    @PostMapping("/tags/delete")
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length == 0) {
            return ResultGenerator.getFailResult("参数错误");
        }
        boolean result = tagService.removeBatchTags(ids);
        if (result) {
            return ResultGenerator.getSuccessResult("删除成功");
        } else {
            return ResultGenerator.getFailResult("删除失败");
        }
    }
}
