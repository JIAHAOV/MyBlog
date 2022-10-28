package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.mapper.AdminRoleRelationMapper;
import com.study.reproduce.model.domain.Role;
import com.study.reproduce.service.RoleService;
import com.study.reproduce.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 18714
* @description 针对表【tb_admin_role(权限表)】的数据库操作Service实现
* @createDate 2022-05-23 21:37:38
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Resource
    RoleMapper roleMapper;

    @Override
    public List<Role> getRolesByAdminId(Long adminId) {
        return roleMapper.getRolesByAdminId(adminId);
    }
}




