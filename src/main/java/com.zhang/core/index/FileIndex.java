package com.zhang.core.index;

import com.zhang.core.model.Thing;

public interface FileIndex {
    //业务 对应数据库的CRUD操作，它是将文件信息建立索引
    void index(Thing thing);
}
