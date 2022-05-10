package com.study.reproduce.mapper;

import com.study.reproduce.model.domain.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 18714
* @description 针对表【tb_blog_tag】的数据库操作Mapper
* @createDate 2022-05-10 19:48:27
* @Entity generator.domain.Tag
*/
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}




