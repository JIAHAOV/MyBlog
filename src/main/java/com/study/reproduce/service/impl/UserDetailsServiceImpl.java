package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.reproduce.exception.ExceptionGenerator;
import com.study.reproduce.model.domain.Admin;
import com.study.reproduce.model.domain.AdminDetails;
import com.study.reproduce.model.domain.AdminRoleRelation;
import com.study.reproduce.model.domain.Role;
import com.study.reproduce.service.AdminRoleRelationService;
import com.study.reproduce.service.AdminService;
import com.study.reproduce.service.RoleService;
import com.study.reproduce.utils.PatternUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    RoleService roleService;

    @Resource
    AdminService adminService;

    @Resource
    AdminRoleRelationService adminRoleRelationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new AuthenticationServiceException("用户名为空");
        }
        if (!PatternUtil.isLegal(username)) {
            throw new AuthenticationServiceException("用户名不合法");
        }
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("login_user_name", username);
        Admin admin = adminService.getOne(wrapper);
        if (admin == null) {
            throw new AuthenticationServiceException("用户名不存在");
        }
        QueryWrapper<AdminRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("admin_user_id", admin.getAdminUserId());
        List<AdminRoleRelation> list = adminRoleRelationService.list(queryWrapper);
        List<Integer> roleIds = list.stream().map(AdminRoleRelation::getRoleId)
                .collect(Collectors.toList());
        List<Role> roles = roleService.listByIds(roleIds);
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
            authorities.add(authority);
        }
        AdminDetails adminDetails = new AdminDetails(admin.getAdminUserId(), username, admin.getLoginPassword(), authorities);
        return adminDetails;
    }
}
