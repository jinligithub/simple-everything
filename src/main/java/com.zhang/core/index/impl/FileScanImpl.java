package com.zhang.core.index.impl;

import com.zhang.config.simpleEverythingConfig;
import com.zhang.core.DAO.DataSourceFactory;
import com.zhang.core.DAO.impl.FileIndexDaoImpl;
import com.zhang.core.index.FileScan;
import com.zhang.core.interceptor.FileInterceptor;
import com.zhang.core.interceptor.impl.FileIndexInterceptor;
import com.zhang.core.interceptor.impl.FilePrintInterceptor;
import com.zhang.core.model.Thing;

import javax.activation.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileScanImpl implements FileScan {

    //排除运算
    private simpleEverythingConfig config= simpleEverythingConfig.getInstance();
    //调用拦截器
    private LinkedList<FileInterceptor> interceptors=new LinkedList<>();


    @Override
    public void index(String path) {
        //给一个路径，进行递归遍历
        File file =new File(path);
        //判断是否为文件，不是排除目录文件添加到fileList里去，是排除目录里的文件直接返回
        //D:\a\b\c\aaa.pdf    ->  D:\a\b
        //先判断一下这个文件的父目录是否在排除出文件里，如果不在就把他添加到fileList里去
        if(file.isFile()){
            if(config.getExcludePath().contains(file.getParent())){
                return ;
            }
        } else{
            //目录    如果这个目录不包含排除目录 才继续往里进展
            if(config.getExcludePath().contains(path)){
                return;
            }else{
                File[] files = file.listFiles();
                //判断路径是否为空，不为空获取绝对路径
                if(files!=null){
                    for(File f:files){
                        index(f.getAbsolutePath());
                    }
                }
            }
        }
        //Index 有两种情况  ：File  Directory
        for(FileInterceptor interceptor:this.interceptors){
            interceptor.apply(file);
        }
    }

    @Override
    public void interceptor(FileInterceptor interceptor) {
        // 添加拦截器
        this.interceptors.add(interceptor);
    }




}
