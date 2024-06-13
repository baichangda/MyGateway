package com.bcd.base.support_parser.anno;

import java.lang.annotation.*;

/**
 * 适用于任何类、会被子类继承
 * 用于标定类中所有解析字段应该占用的字节
 * 如果解析未达到指定长度、则skip
 * 反解析未达到指定长度、则write byte 0
 *
 * 其实现有两种情况、取决于是否能统计出类的所有字段总字节长度、即{@link com.bcd.base.support_parser.util.ParseUtil#getClassByteLenIfPossible(Class)}
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface C_skip {
    /**
     * 占用字节数
     * 1-8
     * 与{@link #lenExpr()}互斥
     */
    int len() default 0;

    /**
     * 字段所占字节长度表达式、可以使用本类中的变量
     * 用于固定长度字段解析,配合var参数使用,代表的是Byte的长度
     * 与{@link #len()}互斥
     * 变量取值来源于var、globalVar
     * 使用globalVar时候必须在变量前面带上@
     * 例如:
     * m
     * m*n
     * a*b-1
     * a*(b-2)
     * a*(b-2)+@a
     */
    String lenExpr() default "";
}
