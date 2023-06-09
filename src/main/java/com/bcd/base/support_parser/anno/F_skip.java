package com.bcd.base.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于任何字段
 * 跳过数个字节
 * 用在字段上面只是为了占位、解析不会对字段进行赋值、反解析也不会使用字段值
 *
 * 反解析中
 * 值可以为null、值对结果无影响
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_skip {
    /**
     * 跳过字节模式
     * {@link SkipMode#skip}
     * 直接跳过当前指定长度的字节数
     * {@link SkipMode#reservedFromStart}
     * 从当前字段所属对象开始记录索引a、当前字段索引b、跳过(规定长度-(b-a)))
     */
    SkipMode mode() default SkipMode.skip;

    /**
     * 占用字节数
     * 1-8
     * 与{@link #lenExpr()}互斥
     */
    int len() default 0;

    /**
     * 字段所占字节长度表达式
     * 用于固定长度字段解析,配合var参数使用,代表的是Byte的长度
     * 与{@link #len()}互斥
     * 例如:
     * m
     * m*n
     * a*b-1
     * a*(b-2)
     */
    String lenExpr() default "";
}
