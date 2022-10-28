package com.study.reproduce.service;

import com.study.reproduce.mapper.AdminRoleRelationMapper;
import com.study.reproduce.model.domain.AdminRoleRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reproduce.model.domain.Role;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 18714
* @description 针对表【tb_admin_role_relation(管理员和权限的关系表)】的数据库操作Service
* @createDate 2022-05-23 21:37:38
*/
public interface AdminRoleRelationService extends IService<AdminRoleRelation> {

}
