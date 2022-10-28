package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.exception.ExceptionGenerator;
import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.model.ov.BlogTagCount;
import com.study.reproduce.model.domain.BlogTagRelation;
import com.study.reproduce.model.domain.Tag;
import com.study.reproduce.service.BlogTagRelationService;
import com.study.reproduce.mapper.BlogTagRelationMapper;
import com.study.reproduce.service.CacheService;
import com.study.reproduce.service.TagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.study.reproduce.constant.RedisConstant.HOT_TAGS_KEY;

/**
* @author 18714
* @description 针对表【tb_blog_tag_relation】的数据库操作Service实现
* @createDate 2022-05-13 21:24:59
*/
@Service
public class BlogTagRelationServiceImpl extends ServiceImpl<BlogTagRelationMapper, BlogTagRelation>
    implements BlogTagRelationService{

    @Resource
    TagService tagService;

    @Resource
    CacheService cacheService;

    @Resource
    BlogTagRelationMapper blogTagRelationMapper;

    @Override
    public boolean updateBlogTagRelation(Blog blog) {
        //获取所有标签
        String[] tagNames = blog.getBlogTags().split(",");
        if (tagNames.length > 6) {
            throw ExceptionGenerator.businessError("标签上限为6个");
        }
        ArrayList<Tag> tags = new ArrayList<>();
        ArrayList<Tag> newTags = new ArrayList<>();
        //找出之前没有的标签
        for (String tagName : tagNames) {
            Tag tag = tagService.getTagByName(tagName);
            if (tag == null) {
                tag = new Tag(tagName, LocalDateTime.now());
                newTags.add(tag);
            }
            tags.add(tag);
        }
        //新增标签,主键自动回填，新增的没有设置 id 的对象，自动设置了 id
        if (newTags.size() > 0) {
            tagService.saveBatch(newTags);
        }
        //TODO tag 使用了逻辑删除，当新增之前删除的标签的时候，复用之前删除的
        //添加关系数据
        ArrayList<BlogTagRelation> relations = new ArrayList<>();
        for (Tag tag : tags) {
            BlogTagRelation relation = new BlogTagRelation(blog.getBlogId(), tag.getTagId());
            relations.add(relation);
        }
        QueryWrapper<BlogTagRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blog.getBlogId());
        //先移除之前的关系
        this.remove(wrapper);
        //更新关系
        return this.saveBatch(relations);
    }

    @Override
    public List<BlogTagCount> getBlogTagCountIndex(Integer temp) {
        return blogTagRelationMapper.getBlogTagCount();
    }

    @Override
    public List<?> getTagCount() {
        List<?> cache = cacheService.getFromCache(HOT_TAGS_KEY, 1, this::getBlogTagCountIndex, List.class, 24L, TimeUnit.HOURS);
        if (cache == null) {
            return this.getBlogTagCountIndex(1);
        }
        return cache;
    }
}




