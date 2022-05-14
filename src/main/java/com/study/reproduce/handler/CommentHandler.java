package com.study.reproduce.handler;

import com.study.reproduce.model.domain.Comment;
import com.study.reproduce.model.request.PageParam;
import com.study.reproduce.model.request.ReplayParam;
import com.study.reproduce.service.CommentService;
import com.study.reproduce.utils.PageResult;
import com.study.reproduce.utils.Result;
import com.study.reproduce.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
public class CommentHandler {

    @Resource
    CommentService commentService;

    @GetMapping("/comments/list")
    public Result list(PageParam pageParam) {
        if (pageParam.getPage() == null || pageParam.getLimit() == null) {
            return ResultGenerator.getFailResult("参数错误");
        }
        PageResult<Comment> pageResult = commentService.queryByPage(pageParam);
        return ResultGenerator.getSuccessResult(pageResult);
    }

    @PostMapping("/comments/checkDone")
    public Result checkDone(@RequestBody List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return ResultGenerator.getFailResult("参数错误");
        }
        if (commentService.checkDone(ids)) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("审核失败");
        }
    }

    @PostMapping("/comments/reply")
    public Result checkDone(ReplayParam replayParam) {
        if (replayParam.getCommentId() == null || replayParam.getReplyBody() == null) {
            return ResultGenerator.getFailResult("参数错误");
        }
        if (commentService.reply(replayParam.getCommentId(), replayParam.getReplyBody())) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("回复失败");
        }
    }

    @PostMapping("/comments/delete")
    public Result delete(@RequestBody List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return ResultGenerator.getFailResult("删除失败");
        }
        if (commentService.removeByIds(ids)) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("删除失败");
        }
    }
}
