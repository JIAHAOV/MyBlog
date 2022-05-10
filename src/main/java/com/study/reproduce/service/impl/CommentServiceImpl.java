package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.model.domain.Comment;
import com.study.reproduce.mapper.CommentMapper;
import com.study.reproduce.service.CommentService;
import org.springframework.stereotype.Service;

/**
* @author 18714
* @description 针对表【tb_blog_comment】的数据库操作Service实现
* @createDate 2022-05-10 19:48:27
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService {

}




