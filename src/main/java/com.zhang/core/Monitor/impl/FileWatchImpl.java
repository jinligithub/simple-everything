package com.zhang.core.Monitor.impl;

import com.zhang.core.DAO.FileIndexDao;
import com.zhang.core.Monitor.FileWatch;
import com.zhang.core.common.FileConvertThing;
import com.zhang.core.common.HandlePath;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileFilter;

public class FileWatchImpl implements FileAlterationListener, FileWatch {
    //把数据库传过来
    private FileIndexDao fileIndexDao;
    //监视器
    private FileAlterationMonitor monitor;

    public FileWatchImpl(FileIndexDao fileIndexDao){
        this.fileIndexDao=fileIndexDao;
        //设置一个文件遍历时间
        this.monitor=new FileAlterationMonitor(100);

    }

    @Override
    public void onStart(FileAlterationObserver observer) {
        //启动监视器，this就是一个当前的监听对象
       // observer.addListener(this);
    }

    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println("onDirectoryCreate"+directory);
    }

    @Override
    public void onDirectoryChange(File directory) {
        System.out.println("onDirectoryChange"+directory);

    }

    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println("onDirectoryDelete"+directory);

    }

    @Override
    public void onFileCreate(File file) {
        //文件创建
        System.out.println("OnFileCreate"+file);
        this.fileIndexDao.insert(FileConvertThing.convert(file));
    }

    @Override
    public void onFileChange(File file) {
        //
        System.out.println("OnFileChange"+file);

    }

    @Override
    public void onFileDelete(File file) {
        //文件删除
        System.out.println("onFileDelete"+file);
        this.fileIndexDao.delete(FileConvertThing.convert(file));
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        //移除监听
        //observer.removeListener(this);
    }

    @Override
    public void start() {
        //启动监听
        try {
            this.monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //监听
    @Override
    public void monitor(HandlePath handlePath) {
        //监控的是includePath
        for(String path:handlePath.getIncludePath()){
            FileAlterationObserver observer=new FileAlterationObserver(path, new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String currentPath = pathname.getAbsolutePath();

                    //遍历排除路径，如果在排除目录里返回false，不在返回true
                    //使用startWith（），可以把该目录下的文件都排除了
                    for(String excludePath:handlePath.getExcludePath()){
                        if(excludePath.startsWith(currentPath)){
                            return false;
                        }
                    }
                    return true;
                }
            });
            observer.addListener(this);
            this.monitor.addObserver(observer);
        }
    }

    @Override
    public void stop() {
        //停止监听
        try {
            this.monitor.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
