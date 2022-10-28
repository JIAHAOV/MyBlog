package com.study.reproduce.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.listener.event.ReplyCommentEvent;
import com.study.reproduce.model.domain.Comment;
import com.study.reproduce.mapper.CommentMapper;
import com.study.reproduce.model.request.PageParam;
import com.study.reproduce.service.CommentService;
import com.study.reproduce.service.SendMailService;
import com.study.reproduce.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.study.reproduce.constant.MailConstant.REPLY_TEMPLATE;
import static com.study.reproduce.constant.MailConstant.REPLY_TITLE;

/**
* @author 18714
* @description 针对表【tb_blog_comment】的数据库操作Service实现
* @createDate 2022-05-10 19:48:27
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ApplicationEventPublisher publisher;

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

        publisher.publishEvent(new ReplyCommentEvent(comment));

        return update > 0;
    }

    @Override
    public PageResult<Comment> getCommentPage(Long blogId, Integer page) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        Page<Comment> commentPage = new Page<>(page, 8);
        wrapper.eq(Comment::getBlogId, blogId)
                .eq(Comment::getCommentStatus, 1);

        Page<Comment> selectPage = commentMapper.selectPage(commentPage, wrapper);
        long totalCount = selectPage.getTotal();
        return new PageResult<>(totalCount, 8,
                page, selectPage.getRecords());
    }

}




