package com.study.reproduce.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class MD5Util {
    /**
     * 盐
     */
    private static final String SALT = "salt";

    /**
     * 对密码进行加密
     * @param password 需要加密的密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        if (password == null) {
            return null;
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
        return encryptPassword;
    }
}
