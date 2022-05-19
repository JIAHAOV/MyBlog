package com.study.reproduce.service;

import com.study.reproduce.model.domain.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reproduce.model.vo.BlogDetail;
import com.study.reproduce.model.vo.BlogForDisplay;
import com.study.reproduce.model.vo.SimpleBlogInfo;
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
     * @param ids 需要删除的 id
     * @return 删除结果
     */
    boolean deleteBlogs(List<Integer> ids);

    /**
     * 获取简单的博客信息，用于主页展示
     * @param type 0:根据点击数排序、1：根据发布时间排序
     * @return 查询结果
     */
    List<SimpleBlogInfo> getSimpleBlogInfoIndex(Integer type);

    /**
     * 获取主页展示用的 blog 集合
     * @param page 当前页
     * @return 查询结果
     */
    PageResult<BlogForDisplay> getBlogsForIndexPage(Integer page);

    /**
     * 根据id获取文章的详细信息
     * @param blogId 文章id
     * @return 文章详细信息
     */
    BlogDetail getBlogDetail(Long blogId);

    /**
     * 根据属性名来查询对应的文章
     * @param typeName 对应名称
     * @param page 当前页
     * @param type 0：按标签名查找、1：按类型名查找
     * @return 查询结果
     */
    PageResult<Blog> getBlogsPageByTypeName(String typeName, Integer page, Integer type);

    /**
     * 根据自定义的SubUrl查找文章
     * @param subUrl 自定义路径
     * @return 查询结果
     */
    BlogForDisplay getBlogDetailBySubUrl(String subUrl);
}
