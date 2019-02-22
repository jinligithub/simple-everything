package com.zhang.core.search;

import com.zhang.core.DAO.DataSourceFactory;
import com.zhang.core.DAO.impl.FileIndexDaoImpl;
import com.zhang.core.model.Condition;
import com.zhang.core.model.Thing;
import com.zhang.core.search.impl.FileSearchImpl;

import java.util.List;

/**
 * 根据condition条件进行数据库的检索
 */

public interface FileSearch {
    List<Thing> search(Condition condition);

//    //检查代码
//    public static void main(String[] args){
//        FileSearch fileSearch= new FileSearchImpl
//                (new FileIndexDaoImpl(DataSourceFactory.dataSource()));
//        List<Thing> list = fileSearch.search(new Condition());
//        System.out.println(list);
//    }
}
