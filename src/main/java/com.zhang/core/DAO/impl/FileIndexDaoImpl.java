package com.zhang.core.DAO.impl;

import com.zhang.core.DAO.DataSourceFactory;
import com.zhang.core.DAO.FileIndexDao;
import com.zhang.core.model.Condition;
import com.zhang.core.model.FileType;
import com.zhang.core.model.Thing;

import javax.sql.DataSource;
import java.lang.String;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileIndexDaoImpl implements FileIndexDao {
    /**
     * 获取数据源
     */
    private  final DataSource dataSource;
    public FileIndexDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void delete(Thing thing) {
        //连接
        Connection connection=null;
        //指令
        PreparedStatement statement=null;
        try{
            //1.获取数据库连接
            connection =dataSource.getConnection();
            //2.准备SQL语句
            //这里按文件的路径删除文件因为不同文件夹下可能有相同名字的文件
            //用like 可以把相同路径下文件都删除，这样可以清理更多的文件
            String sql = "delete from file_index where path like '"+thing.getPath()+"%'";
            //3.准备命令
            statement = connection.prepareStatement(sql);
            //4.设置参数 1
            //5.执行命令
            statement.executeUpdate();

        }catch (SQLException e){
            //打印异常
            e.printStackTrace();
        }finally {
            releaseResource(null,statement,connection);
        }
    }

    @Override
    public List<Thing> search(Condition condition) {
        //把查询的结果保存到things里去
        List<Thing> things = new ArrayList<>();
        //连接
        Connection connection=null;
        //指令
        PreparedStatement statement=null;
        //查询结果
        ResultSet resultSet=null;

        try{
            //1.获取数据库连接
            connection =dataSource.getConnection();
            System.out.println(connection);
            //2.准备SQL语句
            //name： like
            //fileType:=
            //limit:  limit offset
            //orderByAsc:  order by
            //4个问号表示参数占用
            StringBuilder sqlBuilder=new StringBuilder();
            sqlBuilder.append("select name, path, depth, file_type from file_index");
            sqlBuilder.append(" where ")
                    //name匹配原则：前模糊，后模糊，前后模糊(该方法采用)
                    .append(" name like '%")
                    .append(condition.getName())
                    .append("%' ");
            //因为文件类型是可选可不选的，有可能FileType是空的所以需要判断
            //因为用户可能会把文件类型写成小写，所以在这里需要把文件类型转为大写
            if(condition.getFlieType()!=null){
                sqlBuilder.append(" and file_type = '")
                          .append(condition.getFlieType().toUpperCase())
                          .append("' ");
            }
            //limit  ,orderBy 必选的   （先orderBy  在 limit）
            //如果orderByASC为true-->asc升序,为false-->desc降序
            if(condition.getOrderByAsc() != null) {
                sqlBuilder.append(" order by depth ")
                        .append(condition.getOrderByAsc() ? "asc" : "desc");
            }
            //设置偏移量从0开始
            if(condition.getLimit() != null) {
                sqlBuilder.append(" limit ")
                        .append(condition.getLimit())
                        .append(" offset 0 ");
            }
            System.out.println(sqlBuilder.toString());
            //3.准备命令
            statement = connection.prepareStatement(sqlBuilder.toString());
            //4.设置参数 1 2 3 4
            //5.执行命令
           resultSet= statement.executeQuery();
           //6.处理结果
            while(resultSet.next()){
                //数据库中的行记录-->java中的对象Thing
                Thing thing=new Thing();
                thing.setName(resultSet.getString("name"));
                thing.setPath(resultSet.getString("path"));
                thing.setDepth(resultSet.getInt("depth"));
                String fileType=resultSet.getString("file_type");
                //根据文件类型名（String）获取文件类型对象
                thing.setFileType(FileType.lookupByName(fileType));
                //把查询的结果thing放到things里去
                //System.out.println(thing);
                things.add(thing);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
          releaseResource(resultSet,statement,connection);
        }
        //返回查询结果

        return things;
    }

    //解决代码大重复问题  ：重构
    private void releaseResource(ResultSet resultSet,
                                 PreparedStatement statement,Connection connection){
        //当结果集不为空时关闭结果集
        if(resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //命令不等于null
        if(statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //连接不为空
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    //测试插入函数代码
//    public static void main (String[] args){
//        FileIndexDao fileIndexDao=new FileIndexDaoImpl(DataSourceFactory.dataSource());
//        Thing thing= new Thing();
//        thing.setName("课件2.pdf");
//        thing.setPath("C:\\Users\\Z\\Desktop\\资料\\2. 数据结构-初阶课件(C实现）");
//        thing.setDepth(5);
//        thing.setFileType(FileType.DOC);
//
//
//        //fileIndexDao.insert(thing);
//        Condition condition= new Condition();
//        condition.setName("课件2");
//        condition.setLimit(1);
//        condition.setOrderByAsc(true);
//        condition.setFlieType("IMG");
//        List<Thing> things = fileIndexDao.search(condition);
//        for(Thing thing1:things){
//            System.out.println(thing1);
//        }
//    }
    /**
     *
     * @param thing
     */
    @Override
    public void insert(Thing thing) {
        //连接
        Connection connection=null;
        //指令
        PreparedStatement statement=null;
        try{
            //1.获取数据库连接
            connection =dataSource.getConnection();
            //2.准备SQL语句
            //4个问号表示参数占用
            String sql = "insert into file_index(name, path, depth, file_type) values(?,?,?,?)";
            //3.准备命令
            statement = connection.prepareStatement(sql);
            //4.设置参数 1 2 3 4
            statement.setString(1,thing.getName());
            statement.setString(2,thing.getPath());
            statement.setInt(3,thing.getDepth());
            //FileType.DOC-->DOC
            statement.setString(4,thing.getFileType().name());
            //5.执行命令
            statement.executeUpdate();

        }catch (SQLException e){
            //打印异常
            e.printStackTrace();
        }finally {
            releaseResource(null,statement,connection);
        }
    }
}
