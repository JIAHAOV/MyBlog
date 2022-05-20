package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.exception.ExceptionGenerator;
import com.study.reproduce.mapper.BlogTagRelationMapper;
import com.study.reproduce.model.domain.BlogTagRelation;
import com.study.reproduce.model.domain.Tag;
import com.study.reproduce.mapper.TagMapper;
import com.study.reproduce.service.TagService;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author 18714
* @description 针对表【tb_blog_tag】的数据库操作Service实现
* @createDate 2022-05-10 19:48:27
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService {
    @Resource
    TagMapper tagMapper;
    @Resource
    BlogTagRelationMapper blogTagRelationMapper;

    @Override
    public Tag getTagByName(String tagName) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_name", tagName);
        return this.getOne(queryWrapper);
    }

    @Override
    public PageResult<Tag> queryByPageUtil(PageQueryUtil pageQueryUtil) {
        Page<Tag> page = new Page<>(pageQueryUtil.getPage(), pageQueryUtil.getLimit());
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("tag_id");
        Page<Tag> tagPage = tagMapper.selectPage(page, wrapper);
        Long totalCount = tagMapper.selectCount(null);
        PageResult<Tag> pageResult = new PageResult<>(totalCount, pageQueryUtil.getLimit(),
                pageQueryUtil.getPage(), tagPage.getRecords());
        return pageResult;
    }

    @Override
    public boolean saveTag(String tagName) {
        if (tagName.isEmpty()) {
            throw ExceptionGenerator.businessError("参数不能为空");
        }
        if (tagName.length() > 10) {
            throw ExceptionGenerator.businessError("标签名过长");
        }
        //使用正则表达式判断名称是否符合规范
        String valid = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(valid);
        Matcher matcher = pattern.matcher(tagName);
        if (!matcher.find()) {
            throw ExceptionGenerator.businessError("只能包括字母、中文、数字、下划线");
        }
        Tag tag = new Tag(tagName, LocalDateTime.now());
        int result = tagMapper.insert(tag);
        return result > 0;
    }

    @Override
    @Transactional
    public boolean removeBatchTags(Integer[] tagIds) {
        if (tagIds.length == 0) {
            return false;
        }
        for (Integer tagId : tagIds) {
            QueryWrapper<BlogTagRelation> wrapper = new QueryWrapper<>();
            wrapper.eq("tag_id", tagId);
            if (blogTagRelationMapper.selectCount(wrapper) > 0) {
                return false;
            }
            int result = tagMapper.deleteById(tagId);
            if (result <= 0) {
                return false;
            }
        }
        return true;
    }
}




