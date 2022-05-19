package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.exception.ExceptionGenerator;
import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.model.domain.Category;
import com.study.reproduce.mapper.CategoryMapper;
import com.study.reproduce.service.BlogService;
import com.study.reproduce.service.CategoryService;
import com.study.reproduce.utils.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 18714
* @description 针对表【tb_blog_category】的数据库操作Service实现
* @createDate 2022-05-10 19:48:27
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;
    @Resource
    BlogService blogService;

    @Override
    public PageResult<Category> queryCategoryPage(int currentPage, int pageSize) {
        Page<Category> page = new Page<>(currentPage, pageSize);
        page.addOrder(OrderItem.desc("category_id"));
        Page<Category> categoryPage = categoryMapper.selectPage(page, null);
        Long totalCount = categoryMapper.selectCount(null);
        PageResult<Category> pageResult = new PageResult<>();
        pageResult.setTotalCount(totalCount);
        pageResult.setPageSize(pageSize);
        pageResult.setCurrPage(currentPage);
        pageResult.setList(categoryPage.getRecords());
        return pageResult;
    }

    @Override
    @Transactional
    public boolean deleteByIds(List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return false;
        }
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("blog_category_id", ids);
        List<Blog> blogs = blogService.list(queryWrapper);
        if (blogs.size() != 0) {
            for (Blog blog : blogs) {
                blog.setBlogCategoryId(0);
                blog.setBlogCategoryName("默认分类");
            }
            if (!blogService.updateBatchById(blogs)) {
                return false;
            }
        }
        if (categoryMapper.deleteBatchIds(ids) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean saveCategory(Category category) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("category_name", category.getCategoryName());
        if (categoryMapper.selectCount(wrapper) > 0) {
            throw ExceptionGenerator.businessError("名称重复");
        }
        category.setCategoryId(null);
        int insert = categoryMapper.insert(category);
        return insert > 0;
    }
}




