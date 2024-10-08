package com.myvamp.vamp.common;


/**
 * 基于ThreadLocal封装工具类，用于保存并获取当前用户的id
 */

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
