package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.model.domain.Category;
import com.study.reproduce.mapper.CategoryMapper;
import com.study.reproduce.service.CategoryService;
import org.springframework.stereotype.Service;

/**
* @author 18714
* @description 针对表【tb_blog_category】的数据库操作Service实现
* @createDate 2022-05-10 19:48:27
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

}




