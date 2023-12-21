package com.bcd.share.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于任何字段
 * 跳过数个字节
 * 可以配合其他注解一起使用
 * 用在字段上面只是为了占位、解析不会对字段进行赋值、反解析也不会使用字段值
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_skip {
    int lenBefore() default 0;
    String lenExprBefore() default "";

    int lenAfter() default 0;
    String lenExprAfter() default "";
}
