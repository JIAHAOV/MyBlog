package com.study.reproduce.service;

import com.study.reproduce.model.domain.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;

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
    String saveBlog(Blog blog);
}
