package com.study.reproduce.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName cron
 */
@TableName(value ="cron")
@Data
public class Cron implements Serializable {
    /**
     * 
     */
    @TableId
    private String cronId;

    /**
     * 
     */
    private String cron;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}