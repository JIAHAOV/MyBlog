package com.study.reproduce.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.model.domain.Comment;
import com.study.reproduce.mapper.CommentMapper;
import com.study.reproduce.model.request.PageParam;
import com.study.reproduce.service.CommentService;
import com.study.reproduce.service.SendMailService;
import com.study.reproduce.utils.PageResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
* @author 18714
* @description 针对表【tb_blog_comment】的数据库操作Service实现
* @createDate 2022-05-10 19:48:27
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService {

    @Resource
    CommentMapper commentMapper;

    @Resource
    private SendMailService sendMailService;

    private final String REPLY_TEMPLATE = "亲爱的{}:\n" +
            "<br>\n" +
            "你在 <a href=\"http://custom-cloud.xyz/blog/{}\">链接</a> 的评论，已被回复: \"{}\"";
    private final String REPLY_TITLE = "网站回复";

    @Override
    public PageResult<Comment> queryByPage(PageParam pageParam) {
        int currentPage = pageParam.getPage();
        int pageSize = pageParam.getLimit();
        Page<Comment> page = new Page<>(currentPage, pageSize);
        page.addOrder(OrderItem.desc("comment_id"));
        Page<Comment> commentPage = commentMapper.selectPage(page, null);
        Long totalCount = commentMapper.selectCount(null);
        return new PageResult<>(totalCount, pageSize,
                currentPage, commentPage.getRecords());
    }

    @Override
    public boolean checkDone(List<Integer> ids) {
        if (ids.size() == 0) {
            return false;
        }
        List<Comment> comments = this.listByIds(ids);
        for (Comment comment : comments) {
            comment.setCommentStatus(1);
        }
        return this.updateBatchById(comments);
    }

    @Override
    public boolean reply(Long commentId, String replayMessage) {
        if (commentId == null || replayMessage.isEmpty()) {
            return false;
        }
        Comment comment = commentMapper.selectById(commentId);
        if (comment.getCommentStatus().equals(0)) {
            return false;
        }

        comment.setReplyBody(replayMessage);
        LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Comment::getReplyBody, replayMessage)
                .set(Comment::getReplyCreateTime, LocalDateTime.now())
                .eq(Comment::getCommentId, commentId);

        int update = commentMapper.update(comment, updateWrapper);

        sendMail(comment, replayMessage);

        return update > 0;
    }

    @Override
    public PageResult<Comment> getCommentPage(Long blogId, Integer page) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        Page<Comment> commentPage = new Page<>(page, 8);
        wrapper.eq("blog_id", blogId)
                .eq("comment_status", 1);
        Long totalCount = commentMapper.selectCount(wrapper);
        Page<Comment> selectPage = commentMapper.selectPage(commentPage, wrapper);
        return new PageResult<>(totalCount, 8,
                page, selectPage.getRecords());
    }

    @Async
    public void sendMail(Comment comment, String replayMessage) {
        String mailMessage = StrUtil.format(REPLY_TEMPLATE, comment.getCommentator(), comment.getBlogId(), replayMessage);
        sendMailService.sendHtmlMessage(comment.getEmail(), REPLY_TITLE, mailMessage);
    }
}




