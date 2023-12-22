package com.bcd.share.support_parser.anno;

import java.lang.annotation.*;

/**
 * 适用于任何类
 * 用于标定类中所有解析字段应该占用的字节
 * 如果解析未达到指定长度、则skip
 * 反解析未达到指定长度、则write byte 0
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
     * 例如:
     * m
     * m*n
     * a*b-1
     * a*(b-2)
     */
    String lenExpr() default "";

    /**
     * 忽略参与统计的字段
     * 格式为
     * 类名.属性名
     *
     * 注意
     * 忽略的是字段本身的长度、不会忽略其上面的{@link F_skip}长度
     */
    String[] ignoreFields() default {};
}
