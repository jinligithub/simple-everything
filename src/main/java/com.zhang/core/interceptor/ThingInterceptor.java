package com.zhang.core.interceptor;

import com.zhang.core.model.Thing;

import java.io.File;

@FunctionalInterface
public interface ThingInterceptor {
    void apply(Thing thing);
}
