package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.model.domain.AdminRoleRelation;
import com.study.reproduce.model.domain.Role;
import com.study.reproduce.service.AdminRoleRelationService;
import com.study.reproduce.mapper.AdminRoleRelationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 18714
* @description 针对表【tb_admin_role_relation(管理员和权限的关系表)】的数据库操作Service实现
* @createDate 2022-05-23 21:37:38
*/
@Service
public class AdminRoleRelationServiceImpl extends ServiceImpl<AdminRoleRelationMapper, AdminRoleRelation>
    implements AdminRoleRelationService{

}




