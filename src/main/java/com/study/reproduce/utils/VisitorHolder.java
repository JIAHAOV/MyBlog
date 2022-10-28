package com.study.reproduce.utils;

import com.study.reproduce.model.ov.Visitor;

public class VisitorHolder {
    private static final ThreadLocal<Visitor> tl = new ThreadLocal<>();

    public static void saveVisitor(Visitor visitor){
        tl.set(visitor);
    }

    public static Visitor getVisitor(){
        return tl.get();
    }

    public static void removeVisitor(){
        tl.remove();
    }
}
