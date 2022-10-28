package com.study.reproduce.mapper;

import com.study.reproduce.model.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 18714
* @description 针对表【tb_admin_role(权限表)】的数据库操作Mapper
* @createDate 2022-05-23 21:37:38
* @Entity generator.domain.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRolesByAdminId(@Param("adminId") Long adminId);
}




