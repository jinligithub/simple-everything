package com.zhang.core.common;

import com.zhang.core.model.FileType;
import com.zhang.core.model.Thing;

import java.io.File;

/**
 * 辅助工具类：把File对象转为Thing对象
 */
public final class FileConvertThing {
    private FileConvertThing(){}

    public static Thing convert(File file){
        Thing thing=new Thing();
        //b把File的属性传给Thing
        thing.setName(file.getName());
        thing.setPath(file.getAbsolutePath());
        thing.setDepth(computerFilePath(file));
        //在这里只能获取文件的扩展名，所以必须根据文件的扩展名获取文件类型
        thing.setFileType(computerFileType(file));

        return thing;
    }
    //计算文件的深度
    private static int computerFilePath(File file){
        int depth=0;
        //获取文件路径，并转化为String数组
        String[] segments= file.getAbsolutePath().split(File.separator);
        depth=segments.length;
        return depth;
    }
    //获取文件类型
    private static FileType computerFileType(File file){
        //如果文件是一个目录，返回OHTER
        if(file.isDirectory()){
            return FileType.OTHER;
        }
        //因为扩展名冲最后一个“.”开始分割
        String fileName=file.getName();
        int index= fileName.lastIndexOf(".");

        //index!=-1说明找到那个点了，否则没找到
        //如果有个文件名叫aaa.  当该文件+1,就会发生越界,
        // 所以需要添加index < fileName.length()-1判断
        if(index!=-1&&index < fileName.length()-1){
            //获取扩展名
           String extend= fileName.substring(index+1);
           //lookup查找文件类型的方法
           return FileType.lookup(extend);
        }else{
            return FileType.OTHER;
        }
    }
}
