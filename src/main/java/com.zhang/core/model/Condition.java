package com.zhang.core.model;

import lombok.Data;

@Data   //生成对应的getter setter toString方法
public class Condition {
    //文件名
    private  String name;
    //文件类型
    private String flieType;
    //限制文件的数目
    private  Integer limit;
    //检索文件信息depth的排序规则：默认true升序，false降序desc
    private  Boolean orderByAsc;
}
