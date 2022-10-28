package com.study.reproduce.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.constant.VerifyCode;
import com.study.reproduce.exception.ExceptionGenerator;
import com.study.reproduce.model.domain.Admin;
import com.study.reproduce.service.AdminService;
import com.study.reproduce.mapper.AdminMapper;
import com.study.reproduce.utils.MD5Util;
import com.study.reproduce.utils.PatternUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import static com.study.reproduce.constant.RedisConstant.VERIFY_CODE;

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
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Admin login(String account, String password,String verifyCode, HttpSession session) {
        if (StringUtils.isAnyBlank(account, password, verifyCode)) {
            session.setAttribute("errorMsg", "参数错误");
            return null;
        }
        if (!PatternUtil.isLegal(account)) {
            session.setAttribute("errorMsg", "用户名不合法");
            return null;
        }
        String key = VERIFY_CODE + session.getId();
        String loginVerifyCode = stringRedisTemplate.opsForValue().get(key);
        if (loginVerifyCode == null || !loginVerifyCode.equalsIgnoreCase(verifyCode)) {
            session.setAttribute("errorMsg", "验证码错误");
            return null;
        }

        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getLoginUserName, account);
        Admin admin = adminMapper.selectOne(wrapper);

        if (admin == null) {
            session.setAttribute("errorMsg", "用户名或密码错误");
            return null;
        }
        if (!admin.getLoginPassword().equals(MD5Util.encryptPassword(password))) {
            session.setAttribute("errorMsg", "用户名或密码错误");
            return null;
        }
        Admin safelyAdmin = handleAdminMessage(admin);
        
        session.setAttribute("loginUserName", admin.getLoginUserName());

        StpUtil.login(admin.getAdminUserId());
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
            throw ExceptionGenerator.businessError("参数错误");
        }

        LambdaUpdateWrapper<Admin> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Admin::getLoginPassword, MD5Util.encryptPassword(newPassword))
                .eq(Admin::getAdminUserId, adminUserId)
                .eq(Admin::getLoginPassword, MD5Util.encryptPassword(originalPassword));
        return this.update(updateWrapper);
    }

    @Override
    public boolean updateName(Integer adminUserId, String loginUserName, String nickName) {
        if (adminUserId == null || StringUtils.isAnyEmpty(loginUserName, nickName)) {
            throw ExceptionGenerator.businessError("参数错误");
        }

        LambdaUpdateWrapper<Admin> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Admin::getLoginUserName, loginUserName)
                .set(Admin::getNickName, nickName)
                .eq(Admin::getAdminUserId, adminUserId);
        return this.update(updateWrapper);
    }

    @Override
    public Admin getAdminByUsername(String username) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getLoginUserName, username);
        return this.getOne(queryWrapper);
    }
}




