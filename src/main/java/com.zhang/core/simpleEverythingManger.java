package com.zhang.core;
//调度器

import com.zhang.config.simpleEverythingConfig;
import com.zhang.core.DAO.DataSourceFactory;
import com.zhang.core.DAO.FileIndexDao;
import com.zhang.core.DAO.impl.FileIndexDaoImpl;
import com.zhang.core.Monitor.FileWatch;
import com.zhang.core.common.HandlePath;
import com.zhang.core.Monitor.impl.FileWatchImpl;
import com.zhang.core.index.FileScan;
import com.zhang.core.index.impl.FileScanImpl;
import com.zhang.core.interceptor.impl.FileIndexInterceptor;
import com.zhang.core.interceptor.impl.ThingClearInterceptor;
import com.zhang.core.model.Condition;
import com.zhang.core.model.Thing;
import com.zhang.core.search.FileSearch;
import com.zhang.core.search.impl.FileSearchImpl;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class simpleEverythingManger {
    private static volatile simpleEverythingManger manger;
    //文件查找
    private  FileSearch fileSearch;
    //文件扫描
    private FileScan fileScan;
    //设置线程池的数目
    private ExecutorService executorService ;
    //清理要删除的文件
    private ThingClearInterceptor thingClearInterceptor;
    private Thread backgroundClearThread;
    //标识变量,用于清理线程
    private AtomicBoolean backgroundClearThreadStatus = new AtomicBoolean(false);
    //文件监控对象
    private FileWatch fileWatch;


    private simpleEverythingManger(){
        this.initComponrnt();
    }
    private void initComponrnt(){
        //数据源对象
        DataSource dataSource= DataSourceFactory.dataSource();
        //检查数据库
        initOrResetDatabase();
        //业务层对象
        FileIndexDao fileIndexDao=new FileIndexDaoImpl(dataSource);
        this.fileSearch=new FileSearchImpl(fileIndexDao);
        this.fileScan=new FileScanImpl();
        //调用拦截器
        //this.fileScan.interceptor(new FilePrintInterceptor());//建立索引的时候不需要打印文件路径
        this.fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));

        this.thingClearInterceptor=new ThingClearInterceptor(fileIndexDao);
        /**
         * 清理文件线程(默认为用户线程)
         * 守护线程。当清理线程退出时，清理文件线程应该退出
         */
        this.backgroundClearThread=new Thread(this.thingClearInterceptor);
        this.backgroundClearThread.setName("Thread-Thing-Clear");
        this.backgroundClearThread.setDaemon(true);
        //文件监控对象
        this.fileWatch=new FileWatchImpl(fileIndexDao);
    }
//数据源方面的


    public void initOrResetDatabase(){
        DataSourceFactory.initDatabase();
    }
//检查数据库的的业务代码
    public static simpleEverythingManger getInstance(){
        if(manger==null){
            synchronized (simpleEverythingManger.class){
                if(manger==null){
                    manger=new simpleEverythingManger();
                }
            }
        }
        return manger;
    }

    public simpleEverythingManger(FileSearch fileSearch, FileScan fileScan) {
        this.fileSearch = fileSearch;
        this.fileScan = fileScan;
    }
    /**
     * 检索
     */
    public List<Thing> search(Condition condition){
        //利用Stream把不存在的文件剔除掉
        return this.fileSearch.search(condition).stream().filter(thing ->  {
            String path=thing.getPath();
            //获取文件路径
            File file=new File(path);
            //判断文件是否存在
            boolean flag=file.exists();
            if(!flag){
                //做删除操作
                thingClearInterceptor.apply(thing);
            }
            return flag;
            //把Stream流变成list   JDK1.8特性
        }).collect(Collectors.toList());//把Stream流变成list   JDK1.8特性
    }
    /**
     * 构建索引
     */
    public void buildIndex(){
        initOrResetDatabase();
        //用一个集合获取文件路径
        Set<String> directories= simpleEverythingConfig.getInstance().getIncludePath();
        System.out.println(directories.size());
        if(this.executorService==null){
            //根据电脑磁盘的数目，来设置线程池里线程的数目
            this.executorService=Executors.newFixedThreadPool(directories.size(),
                    new ThreadFactory() {

                //设置线程的默认ID为0
                private final AtomicInteger threadId = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    //每new一个线程给线程ID+1
                    thread.setName("Thread-Scan-"+threadId.getAndIncrement());
                    return thread;
                }
            });
        }
        final CountDownLatch countDownLatch = new CountDownLatch(directories.size());


        System.out.println("build index start...");

        //通过这种方式把文件一遍历的同时，把文件一个一个索引
        for( String path:directories){
            this.executorService.submit(()-> {
                simpleEverythingManger.this.fileScan.index(path);
                //当前任务完成，值减一
                countDownLatch.countDown();
            });
        }
        //调用await方法，阻塞进程直到任务完成
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("build index end ...");
    }

    /**
     * 启动清理线程
     */
    public void startBackgroundClearThread(){
        //如果线程的状态为false,和期待值一样启动线程
        if(this.backgroundClearThreadStatus.compareAndSet(false,true)){
            this.backgroundClearThread.start();
        }else{
            System.out.println("不能重复启动线程");
        }
    }
    //启动文件监听系统
    public void startFileSystemMonitor(){
        simpleEverythingConfig config=simpleEverythingConfig.getInstance();
        //要处理的路径
        HandlePath handlePath=new HandlePath();
        handlePath.setIncludePath(config.getIncludePath());
        handlePath.setExcludePath(config.getExcludePath());
        this.fileWatch.monitor(handlePath);
        //在这里启动文件监听线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("文件系统监控启动");
                fileWatch.start();
            }
        }).start();
        this.fileWatch.start();
    }
}

