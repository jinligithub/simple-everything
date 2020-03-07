package com.zhang.core.interceptor;


import java.io.File;

/**
 * 拦截器
 */

@FunctionalInterface//函数式编程接口
public interface FileInterceptor {
    void apply(File file);

}

