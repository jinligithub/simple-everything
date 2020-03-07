package com.zhang.config;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
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
@ToString
public  class simpleEverythingConfig {
    private static volatile simpleEverythingConfig config;
    //建立索引路径
    private Set<String> includePath = new HashSet<>();
    //排除索引文件路径
    private Set<String> excludePath = new HashSet<>();
    //TODO 可配置的参数可能在这里体现
    /**
     * 检索最大的返回值数量。默认为30
     */
    @Setter
    private Integer maxReturn=30;
    /**
     * 深度排序的规则，默认升序
     * order by depth asc limit 30 offset
     */
    @Setter
    private Boolean depthOrderByAsc=true;
    /**
     * H2数据库文件路径
     */
    private String h2IndexPath= System.getProperty("user.dir")+ File.separator+"simple_everything";

    private  simpleEverythingConfig(){
    }
    private void initDefaultPathsConfig(){
        //1.获取文件系统，
        FileSystem fileSystem= FileSystems.getDefault();
        //获取根目录，磁盘   （可迭代说明他为一个集合，因为集合可以迭代）
        //2.遍历的目录
        Iterable<Path> iterable = fileSystem.getRootDirectories();
        iterable.forEach(path -> config.includePath.add(path.toString()));
//        config.getIncludePath().add("G:\\test");
//        config.getExcludePath().add("G:\\test\\abc");
        //排出的目录
        //windows:C:\Windows目录，C:\Program Files (x86),
        // C:\Program Files ,C:\ProgramData

        //Linux:  /tmp/etc
        String osName= System.getProperty("os.name");
        if(osName.startsWith("Windows")){
            Set<String> ex = config.getExcludePath();
            //要排除的文件
            ex.add("C:\\Windows");
            ex.add("G:\\test\\abc");
            ex.add("C:\\Program Files (x86)");
            ex.add("C:\\Program Files");
            ex.add("D:\\Program Files");
            ex.add("D:\\Program Files (x86)");
            ex.add("C:\\ProgramData");
            ex.add("C:\\Users");
        }else{
            //处理Linux
            config.getExcludePath().add("/tmp");
            config.getExcludePath().add("C:/etc");
            config.getExcludePath().add("C:/root");
        }
    }
    public static simpleEverythingConfig getInstance(){
        if(config==null){
            synchronized (simpleEverythingConfig.class){
                if(config==null){
                    config =new simpleEverythingConfig();
                    //initDefaultPathsConfig();写这里防止空指针异常。也可以写成饿汉式进行双重检查
                    config.initDefaultPathsConfig();
                }
            }
        }
        return config;
    }

//测试
    /**
    public static void main(String[] args) {
       // 测试是否遍历的磁盘
        FileSystem fileSystem= FileSystems.getDefault();
        //获取根目录，磁盘   （可迭代说明他为一个集合，因为集合可以迭代）
        Iterable<Path> iterable = fileSystem.getRootDirectories();
        iterable.forEach(new Consumer<Path>() {
            @Override
            public void accept(Path path) {
                System.out.println(path);
            }
        });
       // 获取操作系统的名字
        String osName= System.getProperty("os.name");
        System.out.println(osName);
        //测试排除的路径是否正确
        simpleEverythingConfig config= simpleEverythingConfig.getInstance();
        System.out.println(config.getIncludePath());
        System.out.println(config.getExcludePath());
    }
     */
}
