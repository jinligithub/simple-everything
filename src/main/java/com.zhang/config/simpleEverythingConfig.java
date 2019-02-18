package com.zhang.config;


import lombok.Getter;

import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 配置一些信息，关于把哪些路径要扫描，哪些路径不扫描
 * @Getter防止把集合修改掉
 */
@Getter
public  class simpleEverythingConfig {
    private static volatile simpleEverythingConfig config;
    //建立索引路径
    private Set<String> includePath = new HashSet<>();
    //排除索引文件路径
    private Set<String> excludePath = new HashSet<>();




    private  simpleEverythingConfig(){

    }

    public static simpleEverythingConfig getInstance(){
        if(config==null){
            synchronized (simpleEverythingConfig.class){
                if(config==null){
                    config =new simpleEverythingConfig();
                    //遍历的目录
                    //获取文件系统，
                    FileSystem fileSystem= FileSystems.getDefault();
                    //获取根目录，磁盘   （可迭代说明他为一个集合，因为集合可以迭代）
                    Iterable<Path> iterable = fileSystem.getRootDirectories();
                    //排出的目录
                }
            }
        }

        return config;
    }

    public static void main(String[] args) {
        FileSystem fileSystem= FileSystems.getDefault();
        //获取根目录，磁盘   （可迭代说明他为一个集合，因为集合可以迭代）
        Iterable<Path> iterable = fileSystem.getRootDirectories();
        iterable.forEach(new Consumer<Path>() {
            @Override
            public void accept(Path path) {
                System.out.println(path);
            }
        });
    }

}
