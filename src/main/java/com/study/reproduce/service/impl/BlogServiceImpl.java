package com.study.reproduce.service.impl;
import java.time.LocalDateTime;
import java.util.*;

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
    @Resource
    TagService tagService;

    @Override
    public PageResult<Blog> queryByPageUtil(PageQueryUtil queryUtil) {
        Page<Blog> page = new Page<>(queryUtil.getStart(), queryUtil.getLimit());
        Page<Blog> selectPage = blogMapper.selectPage(page, null);
        Long count = blogMapper.selectCount(null);
        return new PageResult<Blog>(Math.toIntExact(count), queryUtil.getLimit(), queryUtil.getPage(), selectPage.getRecords());
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
        int insert = blogMapper.insert(blog);
        if (insert <= 0) {
            return "新增失败";
        }
        //获取所有标签
        String[] tagNames = blog.getBlogTags().split(",");
        if (tagNames.length > 6) {
            return "标签上限为6个";
        }
        ArrayList<Tag> tags = new ArrayList<>();
        ArrayList<Tag> newTags = new ArrayList<>();
        //找出之前没有的标签
        for (String tagName : tagNames) {
            Tag tag = tagService.getTagByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setTagName(tagName);
                newTags.add(tag);
            }
            tags.add(tag);
        }
        //新增标签,主键自动回填，新增的没有设置 id 的对象，自动设置了 id
        if (newTags.size() > 0) {
            tagService.saveBatch(newTags);
        }
        //添加关系数据
        ArrayList<BlogTagRelation> relations = new ArrayList<>();
        for (Tag tag : tags) {
            BlogTagRelation relation = new BlogTagRelation(blog.getBlogId(), tag.getTagId());
            relations.add(relation);
        }
        if (blogTagRelationService.saveBatch(relations)) {
            return "新增成功";
        }
        return "新增失败";
    }
}




