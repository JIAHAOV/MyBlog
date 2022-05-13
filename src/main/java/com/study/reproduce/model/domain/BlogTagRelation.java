package com.study.reproduce.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName tb_blog_tag_relation
 */
@TableName(value ="tb_blog_tag_relation")
@Data
@NoArgsConstructor
public class BlogTagRelation implements Serializable {
    /**
     * 关系表id
     */
    @TableId(type = IdType.AUTO)
    private Long relationId;

    /**
     * 博客id
     */
    private Long blogId;

    /**
     * 标签id
     */
    private Integer tagId;

    /**
     * 添加时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public BlogTagRelation(Long blogId, Integer tagId) {
        this.blogId = blogId;
        this.tagId = tagId;
    }
}