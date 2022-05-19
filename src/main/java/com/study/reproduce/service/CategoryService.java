package com.study.reproduce.service;

import com.study.reproduce.model.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reproduce.utils.PageResult;

import java.util.List;

/**
* @author 18714
* @description 针对表【tb_blog_category】的数据库操作Service
* @createDate 2022-05-10 19:48:27
*/
public interface CategoryService extends IService<Category> {

    /**
     * 获取页数种类
     * @param currentPage 当前页
     * @param pageSize 页面大小
     * @return 查询结果
     */
    PageResult<Category> queryCategoryPage(int currentPage, int pageSize);

    /**
     * 批量删除
     * @param ids 需要删除的 id
     * @return 是否成功
     */
    boolean deleteByIds(List<Integer> ids);

    /**
     * 存储 category
     * @param category category
     * @return 是否成功
     */
    boolean saveCategory(Category category);
}
