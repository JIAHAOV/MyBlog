package com.study.reproduce.service;

import com.study.reproduce.model.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 18714
* @description 针对表【tb_admin_role(权限表)】的数据库操作Service
* @createDate 2022-05-23 21:37:38
*/
public interface RoleService extends IService<Role> {

    List<Role> getRolesByAdminId(Long adminId);
}
