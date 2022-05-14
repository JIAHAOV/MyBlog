package com.study.reproduce.service;

import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.model.domain.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reproduce.model.request.PageParam;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;

import java.util.List;

/**
* @author 18714
* @description 针对表【tb_blog_comment】的数据库操作Service
* @createDate 2022-05-10 19:48:27
*/
public interface CommentService extends IService<Comment> {

    PageResult<Comment> queryByPage(PageParam pageParam);

    boolean checkDone(List<Integer> ids);

    boolean reply(Long commentId, String replayMessage);
}
