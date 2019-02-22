package com.zhang.cmd;

import com.sun.javadoc.SourcePosition;
import com.zhang.core.model.Condition;
import com.zhang.core.model.Thing;
import com.zhang.core.simpleEverythingManger;

import com.zhang.config.simpleEverythingConfig;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;



public class everythingSearchCmdApp {
    private static Scanner scanner=new Scanner(System.in);
    public static void main(String[] args){

        //欢迎
        welcome();
        //统一调度器
        simpleEverythingManger manager=simpleEverythingManger.getInstance();
        //启动后台清理现场(还可以写个清理后台现场)
        manager.startBackgroundClearThread();
        //交互式
        interactive(manager);

    }

    private static void interactive(simpleEverythingManger manager) {
        while (true){
            System.out.println("simple-Everything>>");
            String input=scanner.nextLine();
            //优先处理search
            if(input.startsWith("search")){
                //search name[file_type]
                String[] values=input.split(" ");
                if(values.length>=2){
                    if(!values[0].equals("search")){
                        help();
                        continue;
                    }
                    Condition condition=new Condition();
                    String name=values[1];
                    condition.setName(name);
                    if(values.length>=3){
                        String fileType=values[2];
                        condition.setFlieType(fileType.toUpperCase());
                    }
                    System.out.println(condition);
                    search(manager ,condition);
                    continue;
                }else {
                    help();
                    continue;
                }
            }
            switch (input){
                case "help":
                    help();
                    break;
                case "quit":
                    quit();
                    break;
                case "index":
                    index(manager);






                    break;
                default:
                    help();
            }
        }
    }

    private static void search(simpleEverythingManger manager, Condition condition) {
        System.out.println("检索功能");
        //统一调度器中的search
        //name fileType  limit orderByAsc
        //condition.setLimit(simpleEverythingConfig.getInstance().;
        //检索内容到List集合
        List<Thing> thingList= manager.search(condition);
        //System.out.println("thingList"+thingList);
        //输出检索的文件路径
        if(thingList != null) {
            for (Thing thing : thingList) {
                System.out.println(thing.getPath());
            }
        }
    }

    private static void index(simpleEverythingManger manager) {
        //统一调度器中的index
        //lamoda表达式
        new Thread(()->manager.buildIndex()).start();
        //方法二：覆写run方法
        //        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                manager.buildIndex();
//            }
//        }).start();
    }

    private static void quit() {
        System.out.println("再见~");
        System.exit(0);
    }
    private static void help() {
        System.out.println("命令列表：");
        System.out.println("退出：quit");
        System.out.println("帮助：help");
        System.out.println("索引：index");
        System.out.println("搜索：search <name> [file-type| img| doc | bin| archive| other]");
    }
    private static void welcome() {
        System.out.println("这是everything-search应用程序的命令行交互");
    }

}
