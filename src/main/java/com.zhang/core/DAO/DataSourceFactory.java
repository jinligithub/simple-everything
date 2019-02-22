package com.zhang.core.DAO;


import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import java.io.*;
import com.zhang.config.simpleEverythingConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataSourceFactory {
    //数据源（单例）
    private static volatile DruidDataSource dataSource;

    //private保证了用户无法实例化对象
    private  DataSourceFactory(){

    }

    //保证了用户可以获取数据源，单例模式（双层检查：保证了多线程环境下不会竞争问题）
    public static DataSource dataSource(){
        if(dataSource==null){
            synchronized (DataSourceFactory.class){
                if(dataSource==null){
                    //获取了一个数据源接口的实现类的实例化对象
                    dataSource=new DruidDataSource();
                    //JDBC关于mysql：jdbc:mysql://ip:port/database
                    dataSource.setDriverClassName("org.h2.Driver");//通过反射实例化了一个driver对象
                    //url,userName,password
                    //采用的是H2的嵌入式数据库，数据库以本地文件的方式存储，只需要提供url接口
                    ////获取当前工作路径
                    ////String workDir= System.getProperty("user.dir");
                    //获取当前用户目录
                    //System.getProperty("user.home");

                    /**JDBC规范关于H2:
                     * jdbc:h2:filepath-->存储到本地文件
                     * jdbc:h2:~/filepath-->存储到当前用户的Home目录
                     * jdbc:h2://ip:port/databaseName-->存储到服务器
                     */
                    //获取当前工作路径
                    dataSource.setUrl("jdbc:h2:"+simpleEverythingConfig.getInstance().getH2IndexPath());
                }
            }
        }
        //返回数据源
        return dataSource;
    }
    public static void initDatabase(){
        //1.获取数据源
        DataSource dataSource=DataSourceFactory.dataSource();
        //2.获取sql语句

        /**
         * 不采取绝对路径文件 ：因为当把项目发送给别人路径就发生了改变
         * E:\JAVA\simple-everything\src\main\resources\simple_everything.sql
         *
         * 采取读取classpath文件下的路径：因为当前resource文件下的simple_everything.sql已经存在了
         */

        //try-with-resources
        try(InputStream in =DataSourceFactory.class.getClassLoader().
                getResourceAsStream("simple_everything.sql");){
            if(in==null){
                throw new RuntimeException("Not read init datbase script please check it");
            }
            StringBuilder sqlBuilder = new StringBuilder();
            try(BufferedReader reader= new BufferedReader(new InputStreamReader(in))){
                //上面输入流转化为String
                String line = null;
                while((line = reader.readLine())!=null){
                    if(!line.startsWith("--")){
                        sqlBuilder.append(line);
                    }
                }
            }
            //3.获取数据库连接和名称执行Sql
            String sql =sqlBuilder.toString();
            /**
             * JDBC
             * （驱动加载：在创建数据源的时间已经准备好了）
             * 3.1获取数据库连接：
             * 3.2创建命令
             * 3.3执行SQL语句
             * 3.4关闭连接，流
             */
            //获取数据库连接
            // Connection connection= dataSource.getConnection();
            Connection connection = dataSource.getConnection();
            //创建命令
            PreparedStatement statement=connection.prepareStatement(sql);
            //执行SQL语句
            statement.execute();
            //关闭连接，流
            connection.close();
            statement.close();
        }catch(IOException e){
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
//    public static void main (String[] args){
//        获取数据源
//        DataSurce dataSource=DataSourceFactory.dataSource();
//        System.out.println(dataSource);
//
//        DataSourceFactory.initDatabase();
//    }
}
