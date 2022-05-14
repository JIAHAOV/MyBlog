package com.study.reproduce.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName tb_config
 */
@TableName(value ="tb_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSiteConfig implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer configId;

    /**
     * 配置项的名称
     */
    private String configName;

    /**
     * 配置项的值
     */
    private String configValue;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public WebSiteConfig(Integer configId, String configValue) {
        this.configId = configId;
        this.configValue = configValue;
        this.createTime = LocalDateTime.now();
    }
}