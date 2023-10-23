package com.bcd.share.support_parser.anno;

import com.bcd.share.support_parser.builder.FieldBuilder;
import com.bcd.share.support_parser.processor.Processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于任何字段
 * 用户自己实现解析逻辑
 * <p>
 * {@link #builderClass()}和{@link #processorClass()} 二选一
 * <p>
 * 反解析中
 * 值可以为null、null的含义由定制逻辑自己实现
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface F_customize {
    /**
     * 处理类
     * 必须是{@link Processor}子类
     */
    Class<?> processorClass();

    /**
     * 处理类参数
     * 在new {@link #processorClass()}时候、会传入指定参数
     * 空字符串、则不传入参数
     * 参数类型支持java基础类型和string
     * 例如有3个参数
     * int,String,double
     * 则值可以是
     * "100,\"test\",100.123"
     */
    String processorArgs() default "";

    /**
     * 变量名称
     * 标注此标记的会在解析时候将值缓存,供表达式使用
     * 例如: m,n,a
     */
    char var() default '0';
}
