package com.zhang.core.DAO;

import com.zhang.core.model.Condition;
import com.zhang.core.model.Thing;

import java.util.List;

/**
 * 关于数据库访问层的CRUD
 */
public interface FileIndexDao {

    /**
     * 插入数据Thing
     * @param thing
     */
    public void insert(Thing thing);

    /**
     * 删除操作
     * @param thing
     */
    void delete(Thing thing);

    /**
     * 根据condition条件进行数据库的检索
     * @param condition
     * @return
     */
    List<Thing> search(Condition condition);

}
