package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.model.domain.BlogTagRelation;
import com.study.reproduce.service.BlogTagRelationService;
import com.study.reproduce.mapper.BlogTagRelationMapper;
import org.springframework.stereotype.Service;

/**
* @author 18714
* @description 针对表【tb_blog_tag_relation】的数据库操作Service实现
* @createDate 2022-05-13 21:24:59
*/
@Service
public class BlogTagRelationServiceImpl extends ServiceImpl<BlogTagRelationMapper, BlogTagRelation>
    implements BlogTagRelationService{

}




