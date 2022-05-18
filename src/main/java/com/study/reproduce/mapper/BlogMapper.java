package com.study.reproduce.mapper;

import com.study.reproduce.model.domain.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.reproduce.model.vo.SimpleBlogInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 18714
* @description 针对表【tb_blog】的数据库操作Mapper
* @createDate 2022-05-10 19:47:52
* @Entity generator.domain.Blog
*/
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    /**
     * 查询博客的简单信息并排序
     * @param type 0:点击率排序、1：创建时间排序
     * @param limit 查询个数
     * @return 查询结果
     */
    List<SimpleBlogInfo> getSimpleBlogInfo(@Param("type") Integer type, @Param("limit") Integer limit);
}




