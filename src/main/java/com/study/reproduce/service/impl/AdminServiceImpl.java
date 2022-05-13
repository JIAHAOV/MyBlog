package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.constant.VerifyCode;
import com.study.reproduce.model.domain.Admin;
import com.study.reproduce.service.AdminService;
import com.study.reproduce.mapper.AdminMapper;
import com.study.reproduce.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
* @author 18714
* @description 针对表【tb_admin_user】的数据库操作Service实现
* @createDate 2022-05-09 21:44:56
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{



    @Resource
    private AdminMapper adminMapper;

    @Override
    public Admin login(String account, String password,String verifyCode, HttpSession session) {
        if (StringUtils.isAnyBlank(account, password, verifyCode)) {
            return null;
        }
        //TODO 验证是否有特殊字符
        Object loginVerifyCode = session.getAttribute(VerifyCode.VERIFY_CODE_KEY);
        if (loginVerifyCode == null) {
            return null;
        }
        if (!loginVerifyCode.equals(verifyCode)) {
            return null;
        }
        QueryWrapper<Admin> wrapper = new QueryWrapper<Admin>();
        wrapper.eq("login_user_name", account);
        Admin admin = adminMapper.selectOne(wrapper);
        if (admin == null) {
            return null;
        }
        if (!admin.getLoginPassword().equals(MD5Util.encryptPassword(password))) {
            return null;
        }
        Admin safelyAdmin = handleAdminMessage(admin);
        session.setAttribute(GET_ADMIN_KEY, safelyAdmin);
        session.setAttribute("loginUserName", admin.getLoginUserName());
        return safelyAdmin;
    }

    @Override
    public Admin handleAdminMessage(Admin admin) {
        if (admin == null) {
            return null;
        }
        Admin safelyAdmin = new Admin();
        safelyAdmin.setAdminUserId(admin.getAdminUserId());
        safelyAdmin.setLoginUserName(admin.getLoginUserName());
        safelyAdmin.setLoginPassword(null);
        safelyAdmin.setNickName(admin.getNickName());
        safelyAdmin.setLocked(null);
        return safelyAdmin;
    }

    @Override
    public boolean updatePassword(Integer adminUserId, String originalPassword, String newPassword) {
        if (adminUserId == null || StringUtils.isAnyEmpty(originalPassword, newPassword)) {
            return false;
        }
        UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("login_password", newPassword)
                .eq("admin_user_id", adminUserId)
                .eq("login_password", originalPassword);
        return this.update(updateWrapper);
    }

    @Override
    public boolean updateName(Integer adminUserId, String loginUserName, String nickName) {
        if (adminUserId == null || StringUtils.isAnyEmpty(loginUserName, nickName)) {
            return false;
        }
        UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("login_user_name", loginUserName)
                .set("nick_name", nickName)
                .eq("admin_user_id", adminUserId);
        return this.update(updateWrapper);
    }
}




