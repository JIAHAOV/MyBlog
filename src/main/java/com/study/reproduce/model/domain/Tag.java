package com.study.reproduce.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName tb_blog_tag
 */
@TableName(value ="tb_blog_tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag implements Serializable {
    /**
     * 标签表主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer tagId;

    /**
     * 标签名称
     */
    private String tagName;

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

    public Tag(String tagName, LocalDateTime createTime) {
        this.tagName = tagName;
        this.createTime = createTime;
    }
}