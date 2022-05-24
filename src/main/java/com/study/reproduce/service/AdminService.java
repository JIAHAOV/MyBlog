package com.study.reproduce.service;

import com.study.reproduce.model.domain.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

/**
* @author 18714
* @description 针对表【tb_admin_user】的数据库操作Service
* @createDate 2022-05-09 21:44:56
*/
public interface AdminService extends IService<Admin> {
    /**
     * 存储用户信息的键
     */
    String GET_ADMIN_KEY = "thisIsAdmin";
    /**
     * 处理登录逻辑
     * @param account 账号
     * @param password 密码
     * @param verifyCode 验证码
     * @param session 登录成功后存储到 session 作用域中
     * @return 返回处理过的登录信息
     */
    Admin login(String account, String password,String verifyCode, HttpSession session);
    /**
     * 对用户信息进行脱敏，比如清除密码信息
     * @param admin 需要处理的信息
     * @return 脱敏后的信息
     */
    Admin handleAdminMessage(Admin admin);

    /**
     * 修改密码
     * @param adminUserId 用户id
     * @param originalPassword 原来的密码
     * @param newPassword  修改后密码
     * @return 是否成功
     */
    boolean updatePassword(Integer adminUserId, String originalPassword, String newPassword);

    /**
     * 修改登录账户和昵称
     * @param adminUserId 用户id
     * @param loginUserName 登录账户
     * @param nickName 昵称
     * @return 是否成功
     */
    boolean updateName(Integer adminUserId, String loginUserName, String nickName);

    Admin getAdminByUsername(String username);
}
