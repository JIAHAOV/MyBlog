package com.study.reproduce.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        String encryptPassword = MD5Util.encryptPassword(rawPassword.toString());
        return encryptPassword;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String encode = encode(rawPassword);
        return encode.equals(encodedPassword);
    }
}
