package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.model.domain.Comment;
import com.study.reproduce.mapper.CommentMapper;
import com.study.reproduce.model.request.PageParam;
import com.study.reproduce.service.CommentService;
import com.study.reproduce.utils.PageResult;
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

    @Override
    public PageResult<Comment> queryByPage(PageParam pageParam) {
        int currentPage = pageParam.getPage();
        int pageSize = pageParam.getLimit();
        Page<Comment> page = new Page<>(currentPage, pageSize);
        page.addOrder(OrderItem.desc("comment_id"));
        Page<Comment> commentPage = commentMapper.selectPage(page, null);
        Long totalCount = commentMapper.selectCount(null);
        PageResult<Comment> pageResult = new PageResult<>(totalCount, pageSize,
                currentPage, commentPage.getRecords());
        return pageResult;
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
        UpdateWrapper<Comment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("reply_body", replayMessage)
                .set("reply_create_time", LocalDateTime.now())
                .eq("comment_id", commentId);
        int update = commentMapper.update(comment, updateWrapper);
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
        PageResult<Comment> pageResult = new PageResult<>(totalCount, 8,
                page, selectPage.getRecords());
        return pageResult;
    }
}




