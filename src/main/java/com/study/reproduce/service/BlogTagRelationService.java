package com.study.reproduce.service;

import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.model.domain.BlogTagRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 18714
* @description 针对表【tb_blog_tag_relation】的数据库操作Service
* @createDate 2022-05-13 21:24:59
*/
public interface BlogTagRelationService extends IService<BlogTagRelation> {

    /**
     * 存储新增标签，并同步标签和博客之间的关系
     * @param blog
     * @return
     */
    String updateBlogTagRelation(Blog blog);
}
