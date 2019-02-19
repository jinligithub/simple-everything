package com.zhang.core.index;

import com.zhang.core.model.Thing;

/**
 * 对文件的扫描工作
 */
public interface FileScan {
    //业务 对应数据库的CRUD操作，它是将文件信息建立索引
    //给一个路径进行遍历
    void index(String path);
}
