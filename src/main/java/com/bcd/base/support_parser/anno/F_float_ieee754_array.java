package com.bcd.base.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用如下字段类型
 * float[]、double[]
 *
 * 反解析中
 * 值可以为null、代表空数组
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_float_ieee754_array {

    /**
     * 浮点数类型
     * {@link FloatType_ieee754#Float32}占用4字节
     * {@link FloatType_ieee754#Float64}占用8字节
     */
    FloatType_ieee754 type();


    /**
     * 数组元素个数
     * 与{@link #lenExpr()}互斥
     */
    int len() default 0;

    /**
     * 数组元素个数表达式,配合var参数使用
     * 与{@link #len()}互斥
     * 例如:
     * m
     * m*n
     * a*b-1
     * a*(b-2)
     */
    String lenExpr() default "";

    /**
     * 每个数组元素在读取后、应该skip的byte长度
     */
    int singleSkip() default 0;

    /**
     * 值处理表达式
     * 在解析出的原始值得基础上,进行运算
     * 公式中的x变量代表字段原始的值
     * 注意:
     * 表达式需要符合java运算表达式规则
     * 例如:
     * x-10
     * x*10
     * (x+10)*100
     * (x+100)/100
     */
    String valExpr() default "";

    /**
     * 字节序模式
     */
    ByteOrder order() default ByteOrder.Default;
}
