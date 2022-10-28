package com.study.reproduce.model.ov;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogInfo {
    /**
     * 博客表主键id
     */
    private Long blogId;
    /**
     * 博客标题
     */
    private String blogTitle;
    /**
     * 博客封面图
     */
    private String blogCoverImage;
    /**
     * 博客分类(冗余字段)
     */
    private String blogCategoryName;

    /**
     * 0-草稿 1-发布
     */
    private Integer blogStatus;
    /**
     * 阅读量
     */
    private Long blogViews;
    /**
     * 添加时间
     */
    private LocalDateTime createTime;
}
