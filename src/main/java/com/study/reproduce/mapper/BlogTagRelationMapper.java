package com.study.reproduce.mapper;

import com.study.reproduce.model.ov.BlogTagCount;
import com.study.reproduce.model.domain.BlogTagRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 18714
* @description 针对表【tb_blog_tag_relation】的数据库操作Mapper
* @createDate 2022-05-13 21:24:59
* @Entity generator.domain.BlogTagRelation
*/
public interface BlogTagRelationMapper extends BaseMapper<BlogTagRelation> {
    List<BlogTagCount> getBlogTagCount();
}




