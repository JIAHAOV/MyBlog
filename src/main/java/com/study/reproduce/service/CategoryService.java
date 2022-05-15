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

    PageResult<Category> queryCategoryPage(int currentPage, int pageSize);

    boolean deleteByIds(List<Integer> ids);
}