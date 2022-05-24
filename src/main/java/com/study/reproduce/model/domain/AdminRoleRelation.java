package com.study.reproduce.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 管理员和权限的关系表
 * @TableName tb_admin_role_relation
 */
@TableName(value ="tb_admin_role_relation")
@Data
public class AdminRoleRelation implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer relationId;

    /**
     * 管理员id
     */
    private Integer adminUserId;

    /**
     * 权限id
     */
    private Integer roleId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}