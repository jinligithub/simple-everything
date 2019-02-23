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
        //解析用户参数
        parseParams(args);
       // System.out.println(simpleEverythingConfig.getInstance());//检查config的实例对不对s
        //统一调度器
        simpleEverythingManger manager=simpleEverythingManger.getInstance();
        //启动后台清理现场(还可以写个清理后台现场)
        manager.startBackgroundClearThread();
		//启动监控
        manager.startFileSystemMonitor();
        //交互式
        interactive(manager);

    }

    private static void parseParams(String[] args) {
        //获取实例
        simpleEverythingConfig  config=simpleEverythingConfig.getInstance();
        /**
         * 处理规则：如果用户指定的参数格式不对，使用默认值即可
         */
        for(String param:args){
               String maxReturnParam="--maxReturn=";
              if(param.startsWith(maxReturnParam)){
            //--maxReturn=value
            //indexOf()的用法:返回字符中indexof(string)中字串string在父串中首次出现的位置,从0开始!没有返回-1;方便判断和截取字符串
               int index =param.indexOf("=");
               //为了防止用户输了一个--maxReturn= 后边没有value,这是subString就会出现越界（防止越界），
               //if(index<maxReturnParam.length()-1){
                //获取索引为"="+1开始往后的字符串
                  String maxReturnStr=param.substring(index+1);
                  //方式截取空字符串，抛出异常
                  try{
                      int maxReturn =Integer.parseInt(maxReturnStr);
                      config.setMaxReturn(maxReturn);
                  }catch (NumberFormatException e){
                      //如果用户指定的参数格式不对，使用默认值即可
                  }
                 config.setMaxReturn(Integer.parseInt(maxReturnStr));
              // }
             }
            String depthOrderByAscParam="--depthOrderByAsc=";
            if(param.startsWith(depthOrderByAscParam)){
            //--deprhOrderByAsc=values
                  int index =param.indexOf("=");
                //获取索引为"="+1开始往后的字符串
                   String depthOrderByAscStr=param.substring(index+1);
                   config.setDepthOrderByAsc(Boolean.parseBoolean(depthOrderByAscStr));
            }
            String includePathParam="--includePath=";
            if(param.startsWith(includePathParam)){
            //--includePath=values
               int index =param.indexOf("=");
                //获取索引为"="+1开始往后的字符串
                String includePathStr=param.substring(index+1);
                //用一个数组接收一组路径
               String[] includePath=includePathStr.split(";");
               //防止原来的数据还存在，所以先清理掉原来的数据，在把路径添加进去
               //但是清理路径的时候要先判断，如果为空，就不要再清理的
               if(includePath.length>0) {
                   config.getIncludePath().clear();
               }
                   for(String p:includePath){
                      config.getIncludePath().add(p);
                  }
            }
            String excludePathParam="--excludePath=";
            if(param.startsWith(excludePathParam)){
            //--excludePath=values
               int index =param.indexOf("=");
                //获取索引为"="+1开始往后的字符串
                String excludePathStr=param.substring(index+1);
                //用一个数组接收一组路径
                String[] excludePath=excludePathStr.split(";");
                //防止原来的数据还存在，所以先清理掉原来的数据，在把路径添加进去
                config.getExcludePath().clear();
                for(String p:excludePath){
                    config.getExcludePath().add(p);
                }
           }
        }
    }


    private static void interactive(simpleEverythingManger manager) {
        while (true){
            System.out.println("Simple-Everindexything>>");
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
        System.out.println("检索功能已经开启");
        //统一调度器中的search
        //name fileType  limit orderByAsc
        //设施查询返回的数目和查询的排序顺序
        condition.setLimit(simpleEverythingConfig.getInstance().getMaxReturn());
        condition.setOrderByAsc(simpleEverythingConfig.getInstance().getDepthOrderByAsc());
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
