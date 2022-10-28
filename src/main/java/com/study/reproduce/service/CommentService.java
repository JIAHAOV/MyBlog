package com.study.reproduce.service;

import com.study.reproduce.model.domain.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reproduce.model.request.PageParam;
import com.study.reproduce.utils.PageResult;

import java.util.List;

/**
* @author 18714
* @description 针对表【tb_blog_comment】的数据库操作Service
* @createDate 2022-05-10 19:48:27
*/
public interface CommentService extends IService<Comment> {

    /**
     * 一页页查询评论
     * @param pageParam pageParam
     * @return 查询结果
     */
    PageResult<Comment> queryByPage(PageParam pageParam);

    /**
     * 审核通过评论
     * @param ids 通过的评论id
     * @return 是否成功
     */
    boolean checkDone(List<Integer> ids);

    /**
     * 回复评论
     * @param commentId 回复的评论的id
     * @param replayMessage 回复的内容
     * @return 是否成功
     */
    boolean reply(Long commentId, String replayMessage);

    /**
     * 一页页查询某个文章的评论
     * @param blogId 文章id
     * @param page 当前页
     * @return 查询结果
     */
    PageResult<Comment> getCommentPage(Long blogId, Integer page);
}
