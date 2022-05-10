package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.mapper.BlogMapper;
import com.study.reproduce.service.BlogService;
import org.springframework.stereotype.Service;

/**
* @author 18714
* @description 针对表【tb_blog】的数据库操作Service实现
* @createDate 2022-05-10 19:47:52
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService {

}




