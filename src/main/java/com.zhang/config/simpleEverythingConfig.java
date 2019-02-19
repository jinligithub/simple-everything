package com.zhang.config;


import lombok.Getter;

import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.SQLOutput;
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
                    //1.获取文件系统，
                    FileSystem fileSystem= FileSystems.getDefault();
                    //获取根目录，磁盘   （可迭代说明他为一个集合，因为集合可以迭代）
                    //2.遍历的目录
                    Iterable<Path> iterable = fileSystem.getRootDirectories();
                    iterable.forEach(path -> config.getExcludePath().add(path.toString()));

                    //排出的目录
                    //windows:C:\Windows目录，C:\Program Files (x86),C:\Program Files ,C:\ProgramData
                    //Linux:  /tmp/etc

                    String osName= System.getProperty("os.name");
                    if(osName.startsWith("Windows")){
                        //要排除的文件
                        config.getExcludePath().add("C:\\Windows");
                        config.getExcludePath().add("C:\\Program Files (x86)");
                        config.getExcludePath().add("C:\\Program Files");
                        config.getExcludePath().add("C:\\ProgramData");
                    }else{
                        //处理Linux
                        config.getExcludePath().add("/tmp");
                        config.getExcludePath().add("C:/etc");
                        config.getExcludePath().add("C:/root");
                    }

                }
            }
        }

        return config;
    }
//测试
    public static void main(String[] args) {
        //测试是否遍历的磁盘
//        FileSystem fileSystem= FileSystems.getDefault();
//        //获取根目录，磁盘   （可迭代说明他为一个集合，因为集合可以迭代）
//        Iterable<Path> iterable = fileSystem.getRootDirectories();
//        iterable.forEach(new Consumer<Path>() {
//            @Override
//            public void accept(Path path) {
//                System.out.println(path);
//            }
//        });
        //获取操作系统的名字
        String osName= System.getProperty("os.name");
        System.out.println(osName);
    }

}
