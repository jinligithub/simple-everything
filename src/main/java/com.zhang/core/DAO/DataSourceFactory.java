package com.zhang.core.DAO;

import com.alibaba.druid.pool.DruidAbstractDataSourceMBean;
import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.InputStream;

public class DataSourceFactory {
    private static volatile DruidDataSource dataSource;

    private  DataSourceFactory(){

    }
    public static DataSource dataSource(){
        if(dataSource==null){
            synchronized (DataSourceFactory.class){
                if(dataSource==null){
                    //实例化
                    dataSource=new DruidDataSource();
                    dataSource.setDriverClassName("org.h2.Driver");
                    //url,userName,password
                    //采用的是H2的嵌入式数据库，数据库以本地文件的方式存储，只需要提供url接口
                    //获取当前工作路径
                    String workDir= System.getProperty("user.dir");
                    //获取当前用户目录
                    //System.getProperty("user.home");
                    dataSource.setUrl("jdbc:h2:"+workDir+ File.separator+"simple_everything");
                }
            }
        }
        return dataSource;
    }
    public static void initDatabase(){
        //1.获取数据源
        //2.获取sql语句
        InputStream in =DataSourceFactory.class.getClassLoader().
                getResourceAsStream("simple_everything");
        //3.通过数据库连接和名称执行Sql

    }
//    public static void main (String[] args){
        //获取数据源
//        DataSource dataSource=DataSourceFactory.dataSource();
//        System.out.println(dataSource);
 //       DataSourceFactory.initDatabase();
 //   }
}
