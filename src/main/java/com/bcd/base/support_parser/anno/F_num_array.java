package com.bcd.base.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用如下字段类型
 * byte[]、short[]、int[]、long[]、float[]、double[]、enum[]
 * 数组长度=总字节数/singleLen
 * {@link #len()}和{@link #lenExpr()} 二选一、代表字段数组长度
 * <p>
 * 枚举类
 * 要求枚举类必有如下静态方法、例如
 * public enum Example{
 * public static Example fromInteger(int i){}
 * public int toInteger(){}
 * }
 * <p>
 * 反解析中
 * 值可以为null、代表空数组
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_num_array {
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
     * 每个数组元素
     * 读取原始值的数据类型
     * 数据类型
     */
    NumType singleType();

    /**
     * 每个数组元素
     * 值数据类型
     * 对原始值进行{@link #singleValType()}运算后、存储最终值的类型
     * 默认值代表和{@link #singleType()}一样的类型
     *
     * 此属性对java解析程序没有影响、因为java程序在定义bean的field时候已经考虑进去了
     * 主要用于生成其他语言的解析程序时候用到、例如go
     *
     * {@link #singleValExpr()} 为空时
     * 设置为和{@link #singleType()}一样即可
     *
     * {@link #singleValExpr()} 不为空时
     * 此时定义的类型需要考虑存储如下值都不会出现溢出或错误
     * 原始值、运算过程中的值、结果值
     * 因为在解析时候、会先将读取出的原始值{@link #singleType()}转换为{@link #singleValType()}然后再进行{@link #singleValExpr()}运算
     */
    NumType singleValType() default NumType.Default;

    /**
     * 每个数组元素
     * 在读取后、应该skip的byte长度
     */
    int singleSkip() default 0;

    /**
     * 每个数组元素
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
    String singleValExpr() default "";

    /**
     * 每个数组元素
     * 字节序模式
     */
    ByteOrder singleOrder() default ByteOrder.Default;
}
