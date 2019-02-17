package com.zhang.core.index.impl;

import com.zhang.core.index.FileIndex;
import com.zhang.core.model.Thing;

import javax.activation.DataSource;

public class FileIndexImpl implements FileIndex {
    private  final DataSource dataSource;

    public FileIndexImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Thing thing) {

    }
}
