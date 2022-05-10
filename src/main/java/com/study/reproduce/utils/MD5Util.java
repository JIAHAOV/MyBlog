package com.study.reproduce.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class MD5Util {
    private static final String SALT = "salt";

    public static String encryptPassword(String password) {
        if (password == null) {
            return null;
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
        return encryptPassword;
    }
}
