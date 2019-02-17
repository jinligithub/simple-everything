package com.zhang.core.model;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 这是我们的文件类型
 */
public enum FileType {
    IMG("png","jpeg","jpe","gif"),
    DOC("ppt","pptx","doc","docx","pdf"),
    BIN("exe","sh","jar","msi"),
    ARCHIVE("zip","rar"),
    OTHER("*");
    /**
     * 对应文件类型的扩展名集合
     */
   private Set<String> extend =new HashSet<>();
   FileType(String... extend){
       this.extend.addAll(Arrays.asList(extend));
   }

    /**
     * 根据文件扩展名获取文件类型
     * @param extend
     * @return
     * 存在返回文件类型，
     * 不存在返回FileType.OTHER,因为里面只有五个类型，不属于其中四个类型的都属于OTHER，所以不能返回null
     */
   public static FileType lookup(String extend){
       for(FileType fileType:FileType.values()){
           //判断文件类型里是否包含传入的文件类型
           if(fileType.extend.contains(extend)){
               return fileType;
           }
       }
       return FileType.OTHER;
   }
    /**
     * 根据文件类型名（String）获取文件类型对象
     * @param name
     * @return
     * 存在返回文件类型，
     * 不存在返回FileType.OTHER,因为里面只有五个类型，不属于其中四个类型的都属于OTHER，所以不能返回null
     */
    public static FileType lookupByName(String name) {
        for(FileType fileType:FileType.values()){
            //判断文件类型里是否包含传入的文件类型
            if(fileType.name().equals(name)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }

   public static void main(String[] args){
       System.out.println(FileType.lookup("png"));
       System.out.println(FileType.lookup("java"));
       System.out.println(FileType.lookup("doc"));
   }
}
