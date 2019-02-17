package com.zhang.core.search.impl;

import com.zhang.core.model.Condition;
import com.zhang.core.model.Thing;
import com.zhang.core.search.FileSearch;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class FileSearchImpl implements FileSearch {
    private final DataSource dataSource;

    public FileSearchImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<Thing> search(Condition condition) {
        //TODO
        /**
         * 数据库的处理逻辑
         */
        return  new ArrayList<>();
    }
}
