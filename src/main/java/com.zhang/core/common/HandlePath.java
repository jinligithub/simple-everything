package com.zhang.core.common;

import lombok.Data;

import java.util.Set;

/**
 * 要处理的路径
 */
@Data
public class HandlePath  {
    //包含路径
    private Set<String> includePath;
    //排除路径
    private Set<String> excludePath;
}
