package com.study.reproduce.service.impl;
import java.time.LocalDateTime;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.mapper.CategoryMapper;
import com.study.reproduce.mapper.TagMapper;
import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.mapper.BlogMapper;
import com.study.reproduce.model.domain.BlogTagRelation;
import com.study.reproduce.model.domain.Category;
import com.study.reproduce.model.domain.Tag;
import com.study.reproduce.service.BlogService;
import com.study.reproduce.service.BlogTagRelationService;
import com.study.reproduce.service.TagService;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author 18714
* @description 针对表【tb_blog】的数据库操作Service实现
* @createDate 2022-05-10 19:47:52
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService {

    @Resource
    BlogMapper blogMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    BlogTagRelationService blogTagRelationService;

    @Override
    public PageResult<Blog> queryByPageUtil(PageQueryUtil queryUtil) {
        Page<Blog> page = new Page<>(queryUtil.getStart(), queryUtil.getLimit());
        page.addOrder(OrderItem.desc("blog_id"));
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
//        wrapper.orderByDesc("blog_id");//或者使用 wrapper 进行排序
        Page<Blog> selectPage = blogMapper.selectPage(page, null);
        Long count = blogMapper.selectCount(null);
        return new PageResult<>(count, queryUtil.getLimit(), queryUtil.getPage(), selectPage.getRecords());
    }

    @Override
    @Transactional
    public String saveBlog(Blog blog) {
        blog.setCreateTime(LocalDateTime.now());
        //为文章设置种类名称
        Category category = categoryMapper.selectById(blog.getBlogCategoryId());
        if (category == null) {
            //没有就设为默认分类
            blog.setBlogCategoryId(0);
            blog.setBlogCategoryName("默认分类");
        } else {
            blog.setBlogCategoryName(category.getCategoryName());
        }
        //新增
        int insert = blogMapper.insert(blog);
        if (insert <= 0) {
            return "新增失败";
        }
        //同步标签和博客之间的关系
        return blogTagRelationService.updateBlogTagRelation(blog);
    }

    @Override
    @Transactional
    public String updateBlog(Blog blog) {
        Blog oldBlog = blogMapper.selectById(blog.getBlogId());
        if (oldBlog == null) {
            return "查找不到该博客";
        }
        //检测分类是否改变
        if (!oldBlog.getBlogCategoryId().equals(blog.getBlogCategoryId())) {
            Category category = categoryMapper.selectById(blog.getBlogCategoryId());
            blog.setBlogCategoryName(category.getCategoryName());
        }
        //更新博客
        blogMapper.updateById(blog);
        //检查标签是否改变
        if (!oldBlog.getBlogTags().equals(blog.getBlogTags())) {
            return blogTagRelationService.updateBlogTagRelation(blog);
        }
        return "操作成功";
    }
}




