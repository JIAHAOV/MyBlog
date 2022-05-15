package com.study.reproduce.service;

import com.study.reproduce.model.domain.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;

import java.util.List;

/**
* @author 18714
* @description 针对表【tb_blog】的数据库操作Service
* @createDate 2022-05-10 19:47:52
*/
public interface BlogService extends IService<Blog> {

    /**
     * 通过 PageQueryUtil 查询文章
     * @param queryUtil 用来查询的条件
     * @return PageResult
     */
    PageResult<Blog> queryByPageUtil(PageQueryUtil queryUtil);

    /**
     * 存储博客
     * @param blog blog
     * @return 存储结果
     */
    boolean saveBlog(Blog blog);


    /**
     * 修改博客
     * @param blog blog
     * @return 修改结果
     */
    boolean updateBlog(Blog blog);

    /**
     * 批量删除博客，并移除和标签的关系
     * @param ids
     * @return
     */
    boolean deleteBlogs(List<Integer> ids);
}
