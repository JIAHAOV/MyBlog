package com.study.reproduce.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName tb_blog_category
 */
@TableName(value ="tb_blog_category")
@Data
public class Category implements Serializable {
    /**
     * 分类表主键
     */
    @TableId(type = IdType.AUTO)
    private Integer categoryId;

    /**
     * 分类的名称
     */
    private String categoryName;

    /**
     * 分类的图标
     */
    private String categoryIcon;

    /**
     * 分类的排序值 被使用的越多数值越大
     */
    private Integer categoryRank;

    /**
     * 是否删除 0=否 1=是
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}