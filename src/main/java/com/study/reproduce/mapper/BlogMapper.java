package com.study.reproduce.mapper;

import com.study.reproduce.model.domain.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 18714
* @description 针对表【tb_blog】的数据库操作Mapper
* @createDate 2022-05-10 19:47:52
* @Entity generator.domain.Blog
*/
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

}




