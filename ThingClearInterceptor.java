package com.zhang.core.interceptor.impl;

import com.zhang.core.DAO.FileIndexDao;
import com.zhang.core.interceptor.ThingInterceptor;
import com.zhang.core.model.Thing;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ThingClearInterceptor implements ThingInterceptor,Runnable {
    private Queue<Thing> queue=new ArrayBlockingQueue<>(1024);
    private final FileIndexDao fileIndexDao;

    public ThingClearInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void apply(Thing thing) {
        this.queue.add(thing);
    }

    @Override
    public void run() {
        while(true){
            //取一个删除一个
            Thing thing=this.queue.poll();
            //防止thing为空，因为线程启动的是，队列为空，poll（）函数取出的值肯为空
            if(thing!=null){
                fileIndexDao.delete(thing);
            }
            //优化1：批量删除-->修改delete方法，增加批量删除
            //List<Thing> thingList=new ArrayList<>();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
