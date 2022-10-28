package com.study.reproduce.handler.admin;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.study.reproduce.model.domain.Link;
import com.study.reproduce.model.request.PageParam;
import com.study.reproduce.service.LinkService;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;
import com.study.reproduce.common.Result;
import com.study.reproduce.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/admin")
public class LinkHandler {
    @Resource
    LinkService linkService;

    @GetMapping("/links/list")
    public Result list(PageParam pageParam) {
        if (pageParam.getPage() == null || pageParam.getLimit() == null) {
            return ResultGenerator.getFailResult("参数错误");
        }
        PageQueryUtil queryUtil = new PageQueryUtil(pageParam);
        PageResult<Link> pageResult = linkService.queryByPageUtil(queryUtil);
        return ResultGenerator.getSuccessResult(pageResult);
    }

    @SaCheckRole("admin")
    @PostMapping("/links/save")
    public Result save(Link link) {
        log.info("/links/save\t" + "param: " + link);
        Result result = checkLinkInfo(link);
        if (result != null) {
            return result;
        }
        if (linkService.save(link)) {
            return ResultGenerator.getSuccessResult(link);
        } else {
            return ResultGenerator.getFailResult("新增失败");
        }
    }

    @SaCheckRole("admin")
    @PostMapping("/links/update")
    public Result update(Link link) {
        Result result = checkLinkInfo(link);
        if (result != null) {
            return result;
        }
        if (linkService.updateById(link)) {
            link = linkService.getById(link.getLinkId());
            return ResultGenerator.getSuccessResult(link);
        } else {
            return ResultGenerator.getFailResult("修改失败");
        }
    }

    @SaCheckRole("admin")
    @GetMapping("/links/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResultGenerator.getFailResult("参数错误");
        }
        Link link = linkService.getById(id);
        if (link == null) {
            return ResultGenerator.getFailResult("未找到该对象");
        } else {
            return ResultGenerator.getSuccessResult(link);
        }
    }

    @SaCheckRole("admin")
    @PostMapping("/links/delete")
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length == 0) {
            return ResultGenerator.getFailResult("参数错误");
        }
        if (!linkService.removeByIds(Arrays.asList(ids))) {
            return ResultGenerator.getFailResult("删除失败");
        } else {
            return ResultGenerator.getSuccessResult("删除成功");
        }
    }

    public Result checkLinkInfo(Link link) {
        if (link == null) {
            return ResultGenerator.getFailResult("参数错误");
        }
        if (link.getLinkName().length() > 30 || link.getLinkName().isEmpty()) {
            return ResultGenerator.getFailResult("名称过长或为空");
        }
        if (link.getLinkUrl().length() > 300 || link.getLinkUrl().isEmpty()) {
            return ResultGenerator.getFailResult("url过长或为空");
        }
        if (link.getLinkRank() == null) {
            return ResultGenerator.getFailResult("级别不能为空");
        }
        if (link.getLinkType() == null) {
            return ResultGenerator.getFailResult("类型不能为空");
        }
        return null;
    }
}
