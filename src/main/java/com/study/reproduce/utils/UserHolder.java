package com.study.reproduce.utils;


import com.study.reproduce.model.domain.Admin;

public class UserHolder {
    private static final ThreadLocal<Admin> tl = new ThreadLocal<>();

    public static void saveUser(Admin admin){
        tl.set(admin);
    }

    public static Admin getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
