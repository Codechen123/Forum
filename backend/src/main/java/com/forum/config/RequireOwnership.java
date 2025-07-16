package com.forum.config;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireOwnership {
    String resourceType() default ""; // post, comment, user
    
    String resourceIdParam() default "id"; // 参数名
    
    String message() default "您只能操作自己的资源";
} 