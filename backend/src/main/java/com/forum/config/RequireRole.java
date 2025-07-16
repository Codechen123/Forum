package com.forum.config;

import com.forum.entity.User;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    User.Role[] value();
    
    String message() default "您没有权限执行此操作";
} 