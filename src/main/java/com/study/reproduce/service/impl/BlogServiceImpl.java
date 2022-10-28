package com.study.reproduce.service.impl;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.exception.ExceptionGenerator;
import com.study.reproduce.mapper.CategoryMapper;
import com.study.reproduce.mapper.CommentMapper;
import com.study.reproduce.model.domain.*;
import com.study.reproduce.mapper.BlogMapper;
import com.study.reproduce.model.ov.*;
import com.study.reproduce.service.BlogService;
import com.study.reproduce.service.BlogTagRelationService;
import com.study.reproduce.service.CacheService;
import com.study.reproduce.utils.MakeDownUtil;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.study.reproduce.constant.RedisConstant.*;

/**
* @author 18714
* @description 针对表【tb_blog】的数据库操作Service实现
* @createDate 2022-05-10 19:47:52
*/
@Slf4j
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService {

    private static final Integer SIZE = 9;
    private static final String DEFAULT_ICON = "/admin/dist/img/category/01.png";

    @Resource
    BlogMapper blogMapper;
    @Resource
    CommentMapper commentMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    CacheService cacheService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    ElasticSearchService elasticSearchService;
    @Resource
    BlogTagRelationService blogTagRelationService;

    @Override
    public PageResult<Blog> queryByPageUtil(PageQueryUtil queryUtil) {
        Page<Blog> page = new Page<>(queryUtil.getPage(), queryUtil.getLimit());
//        page.addOrder(OrderItem.desc("blog_id"));
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        if (queryUtil.getKeyword() != null) {
            wrapper.like(Blog::getBlogCategoryName, queryUtil.getKeyword())
                    .or().like(Blog::getBlogTitle, queryUtil.getKeyword());
        }
        wrapper.orderByDesc(Blog::getBlogId);//或者使用 wrapper 进行排序
        Page<Blog> selectPage = blogMapper.selectPage(page, wrapper);
        Long count = blogMapper.selectCount(wrapper);
        return new PageResult<>(count, queryUtil.getLimit(), queryUtil.getPage(), selectPage.getRecords());
    }

    @Override
    @Transactional
    public boolean saveBlog(Blog blog) {
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
            throw ExceptionGenerator.businessError("更新失败");
        }
        //同步标签和博客之间的关系
        boolean result = blogTagRelationService.updateBlogTagRelation(blog);
        //更新缓存
        updateBlogCache(blog);
        saveBlog2Es(blog);
        return true;
    }

    private void updateBlogCache(Blog blog) {
        if (blog.getBlogStatus().equals(0)) {
            return;
        }
        //更新用于分页查询的数据
        String key = BLOG_KEY + "list";
        BlogInfo blogInfo = BeanUtil.copyProperties(blog, BlogInfo.class);
        String jsonStr = JSONUtil.toJsonStr(blogInfo);
        stringRedisTemplate.opsForZSet().removeRangeByScore(key, blogInfo.getBlogId(), blogInfo.getBlogId());
        stringRedisTemplate.opsForZSet().addIfAbsent(key, jsonStr, blogInfo.getBlogId());
        //删除最新博客列表
        stringRedisTemplate.delete(NEW_BLOG_KEY);
        stringRedisTemplate.delete(HOT_TAGS_KEY);
    }

    @Override
    @Transactional
    public boolean updateBlog(Blog blog) {
        Blog oldBlog = blogMapper.selectById(blog.getBlogId());
        if (oldBlog == null) {
            throw ExceptionGenerator.businessError("查找不到该博客");
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
        updateBlogCache(blog);
        saveBlog2Es(blog);
        return true;
    }

    @Async
    void saveBlog2Es(Blog blog) {
        try {
            boolean result = elasticSearchService.updateBlog(blog);
            if (result) {
                log.info("update blog info to es, id is {}", blog.getBlogId());
            } else {
                log.warn("update blog info failed, id is {}", blog.getBlogId());
            }
        } catch (IOException e) {
            log.warn("update blog info failed, id is {}", blog.getBlogId());
        }
    }
    @Async
    void deleteBlog2Es(List<Long> ids) {
        try {
            boolean result = elasticSearchService.deleteBlog(ids);
            if (result) {
                log.info("delete blog info form es, id is {}", ids);
            } else {
                log.warn("delete blog info failed, id is {}", ids);
            }
        } catch (IOException e) {
            log.warn("delete blog info failed, id is {}", ids);
        }
    }

    @Override
    @Transactional
    public boolean deleteBlogs(List<Long> ids) {
        QueryWrapper<BlogTagRelation> wrapper = new QueryWrapper<>();
        wrapper.in("blog_id", ids);
        if (blogTagRelationService.count(wrapper) > 0) {
            if (!blogTagRelationService.remove(wrapper)) {
                return false;
            }
        }
        boolean result = this.removeByIds(ids);
        if (!result) {
            return false;
        }
        deleteBlogCache(ids);
        deleteBlog2Es(ids);
        return true;
    }

    private void deleteBlogCache(List<Long> ids) {
        String key = BLOG_KEY + "list";
        for (Long id : ids) {
            stringRedisTemplate.opsForZSet().removeRangeByScore(key, id, id);
        }
        stringRedisTemplate.delete(HOT_BLOG_KEY);
        stringRedisTemplate.delete(HOT_TAGS_KEY);
        stringRedisTemplate.delete(NEW_BLOG_KEY);
    }

    @Override
    public List<SimpleBlogInfo> getSimpleBlogInfoIndex(Integer type) {
        return blogMapper.getSimpleBlogInfo(type, 9);
    }

    @Override
    public PageResult<BlogForDisplay> getBlogsForIndexPage(Integer page) {
        Page<Blog> blogPage = new Page<>(page, SIZE);
//        blogPage.addOrder(OrderItem.desc("blog_id"));
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_status", 1)
                .orderByDesc("blog_id");
        //查询出一页文章信息
        List<Blog> blogList = blogMapper.selectPage(blogPage, queryWrapper).getRecords();
        blogList = blogList.stream().filter(blog -> blog.getBlogStatus() != 0).collect(Collectors.toList());
        //用来在主页展示的文章对象的集合
        ArrayList<BlogForDisplay> displays = new ArrayList<>();
        List<Category> categories = categoryMapper.selectList(null);
        //提取出 category 的 id 和 icon 映射成 map
        Map<Integer, String> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getCategoryId, Category::getCategoryIcon));
        for (Blog blog : blogList) {
            BlogForDisplay blogForDisplay = new BlogForDisplay();
            //将blog中的相应属性复制到展示用blog
            BeanUtils.copyProperties(blog, blogForDisplay);
            if (categoryMap.containsKey(blogForDisplay.getBlogCategoryId())) {
                //设置 icon 属性
                blogForDisplay.setBlogCategoryIcon(categoryMap.get(blogForDisplay.getBlogCategoryId()));
            } else {
                //没有就设置默认属性
                blogForDisplay.setBlogCategoryId(0);
                blogForDisplay.setBlogCategoryName("默认分类");
                blogForDisplay.setBlogCategoryIcon(DEFAULT_ICON);
            }
            displays.add(blogForDisplay);
        }
        Long totalCount = blogMapper.selectCount(queryWrapper);
        return new PageResult<>(totalCount, SIZE, page, displays);
    }

    @Override
    public BlogDetail getBlogDetail(Long blogId) {
        Blog blog = blogMapper.selectById(blogId);
        BlogDetail blogDetail = new BlogDetail();
        BeanUtils.copyProperties(blog, blogDetail);
        //设置标签
        String[] tags = blog.getBlogTags().split(",");
        blogDetail.setBlogTags(Arrays.asList(tags));
        //设置分类Icon
        if (blog.getBlogCategoryId() == 0) {
            blogDetail.setBlogCategoryIcon(DEFAULT_ICON);
        } else {
            Category category = categoryMapper.selectById(blog.getBlogCategoryId());
            blogDetail.setBlogCategoryIcon(category.getCategoryIcon());
        }
        //将 md 转换成 html
        String htmlContent = MakeDownUtil.markdownToHtmlExtensions(blog.getBlogContent());
        blogDetail.setBlogContent(htmlContent);
        //设置评论数
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blogId);
        Long blogCommentCount = commentMapper.selectCount(wrapper);
        blogDetail.setCommentCount(blogCommentCount);
        return blogDetail;
    }

    @Override
    public BlogDetail getBlogDetailFromCache(Long blogId) {
        BlogDetail blogDetail = cacheService.getFromCache(BLOG_KEY, blogId, this::getBlogDetail, BlogDetail.class, 24L, TimeUnit.HOURS);
        Long view = cacheService.incrementView(blogId);
        if (blogDetail == null) {
            BlogDetail detail = getBlogDetail(blogId);
            detail.setBlogViews(view);
            return detail;
        }
        blogDetail.setBlogViews(view);
        return blogDetail;
    }

    @Override
    public PageResult<Blog> getBlogsPageByTypeName(String typeName, Integer page, Integer type) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        if (type == 0) {
            wrapper.like("blog_tags", typeName);
        } else {
            wrapper.eq("blog_category_name", typeName);
        }
        wrapper.eq("blog_status", 1);
        Page<Blog> blogPage = new Page<>(page, 9);
        Page<Blog> selectPage = blogMapper.selectPage(blogPage, wrapper);
        Long totalCount = selectPage.getTotal();
        PageResult<Blog> pageResult = new PageResult<>(totalCount, 9,
                page, selectPage.getRecords());
        return pageResult;
    }

    @Override
    public BlogForDisplay getBlogDetailBySubUrl(String subUrl) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_sub_url", subUrl);
        BlogForDisplay blogForDisplay = new BlogForDisplay();
        Blog blog = blogMapper.selectOne(wrapper);
        if (blog == null) {
            throw ExceptionGenerator.businessError("资源不存在");
        }
        BeanUtils.copyProperties(blog, blogForDisplay);
        if (blog.getBlogCategoryId() == 0) {
            blogForDisplay.setBlogCategoryIcon(DEFAULT_ICON);
        } else {
            Category category = categoryMapper.selectById(blog.getBlogCategoryId());
            blogForDisplay.setBlogCategoryIcon(category.getCategoryIcon());
        }
        return blogForDisplay;
    }

    @Override
    public PageResult<BlogInfo> queryBlogInfoByPage(Integer currentPage, Integer pageSize) {
        return cacheService.queryPageResult(BLOG_KEY + "list", currentPage, pageSize,
                BlogInfo.class, (o1, o2) -> Math.toIntExact(o2.getBlogId() - o1.getBlogId()));
    }

    @Override
    public List<?> getNewBlogsFromCache() {
        List<?> cache = cacheService.getFromCache(NEW_BLOG_KEY, 1, this::getSimpleBlogInfoIndex, List.class, 5L, TimeUnit.HOURS);
        if (cache == null) {
            return getSimpleBlogInfoIndex(1);
        }
        return cache;
    }

    @Override
    public List<?> getHotBlogs() {
        List<?> cache = cacheService.getFromCache(HOT_BLOG_KEY, 0, this::getSimpleBlogInfoIndex, List.class, 5L, TimeUnit.MINUTES);
        if (cache == null) {
            return getSimpleBlogInfoIndex(0);
        }
        return cache;
    }

    @Override
    public PageResult<BlogInfo> search(Integer page, String keyword) {
        keyword = keyword.toLowerCase(Locale.ENGLISH);
        return elasticSearchService.search(page, keyword);
    }
}




