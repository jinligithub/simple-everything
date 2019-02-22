package com.zhang.core.interceptor.impl;

import com.zhang.core.DAO.FileIndexDao;
import com.zhang.core.common.FileConvertThing;
import com.zhang.core.interceptor.FileInterceptor;
import com.zhang.core.model.Thing;

import java.io.File;

public class FileIndexInterceptor implements FileInterceptor {

    private final FileIndexDao fileIndexDao;

    public FileIndexInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void apply(File file) {
        Thing thing = FileConvertThing.convert(file);
        //TODO 索引的打印
        System.out.println("Thing Insert-->"+thing);
        fileIndexDao.insert(thing);
    }
}
