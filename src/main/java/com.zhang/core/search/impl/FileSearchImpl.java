package com.zhang.core.search.impl;

import com.zhang.core.DAO.FileIndexDao;
import com.zhang.core.model.Condition;
import com.zhang.core.model.Thing;
import com.zhang.core.search.FileSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务层
 */
public class FileSearchImpl implements FileSearch {
    private final FileIndexDao fileIndexDao;

    public FileSearchImpl(FileIndexDao fileIndexDao) {

        this.fileIndexDao = fileIndexDao;
    }


    @Override
    public List<Thing> search(Condition condition) {
        /**
         * 数据库的处理逻辑
         */
        return  this.fileIndexDao.search(condition);
    }
}
