package com.study.reproduce.service.impl;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.exception.ExceptionGenerator;
import com.study.reproduce.mapper.CategoryMapper;
import com.study.reproduce.mapper.CommentMapper;
import com.study.reproduce.model.domain.*;
import com.study.reproduce.mapper.BlogMapper;
import com.study.reproduce.model.vo.BlogDetail;
import com.study.reproduce.model.vo.BlogForDisplay;
import com.study.reproduce.model.vo.SimpleBlogInfo;
import com.study.reproduce.service.BlogService;
import com.study.reproduce.service.BlogTagRelationService;
import com.study.reproduce.utils.MakeDownUtil;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;
import org.springframework.beans.BeanUtils;
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

    private static final Integer SIZE = 9;
    private static final String DEFAULT_ICON = "/admin/dist/img/category/01.png";

    @Resource
    BlogMapper blogMapper;
    @Resource
    CommentMapper commentMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    BlogTagRelationService blogTagRelationService;

    @Override
    public PageResult<Blog> queryByPageUtil(PageQueryUtil queryUtil) {
        Page<Blog> page = new Page<>(queryUtil.getPage(), queryUtil.getLimit());
//        page.addOrder(OrderItem.desc("blog_id"));
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        if (queryUtil.getKeyword() != null) {
            wrapper.like("blog_category_name", queryUtil.getKeyword())
                    .or().like("blog_title", queryUtil.getKeyword());
        }
        wrapper.orderByDesc("blog_id");//或者使用 wrapper 进行排序
        Page<Blog> selectPage = blogMapper.selectPage(page, wrapper);
        Long count = blogMapper.selectCount(wrapper);
        return new PageResult<>(count, queryUtil.getLimit(), queryUtil.getPage(), selectPage.getRecords());
    }

    @Override
    @Transactional
    public boolean saveBlog(Blog blog) {
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
            throw ExceptionGenerator.businessError("更新失败");
        }
        //同步标签和博客之间的关系
        return blogTagRelationService.updateBlogTagRelation(blog);
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
        return true;
    }

    @Override
    @Transactional
    public boolean deleteBlogs(List<Integer> ids) {
        QueryWrapper<BlogTagRelation> wrapper = new QueryWrapper<>();
        wrapper.in("blog_id", ids);
        if (blogTagRelationService.count(wrapper) > 0) {
            if (!blogTagRelationService.remove(wrapper)) {
                return false;
            }
        }
        return this.removeByIds(ids);
    }

    @Override
    public List<SimpleBlogInfo> getSimpleBlogInfoIndex(Integer type) {
        List<SimpleBlogInfo> simpleBlogInfo = blogMapper.getSimpleBlogInfo(type, 9);
        return simpleBlogInfo;
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
        PageResult<BlogForDisplay> pageResult = new PageResult<>(totalCount, SIZE, page, displays);
        return pageResult;
    }

    @Override
    public BlogDetail getBlogDetail(Long blogId) {
        Blog blog = blogMapper.selectById(blogId);
        blog.setBlogViews(blog.getBlogViews() + 1);
        blogMapper.updateById(blog);
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
            throw ExceptionGenerator.pageNotFound("资源不存在");
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
}




