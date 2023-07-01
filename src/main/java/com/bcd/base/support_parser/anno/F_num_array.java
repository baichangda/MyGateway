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
 *
 * 枚举类
 * 仅支持当{@link #singleLen()}为1、2、4时候、因为默认类型为int、8会产生精度丢失
 * 要求枚举类必有如下静态方法、例如
 * public enum Example{
 * public static Example fromInteger(int i){}
 * public int toInteger(){}
 * }
 *
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
     * 单个元素类型
     */
    NumType singleType();

    /**
     * 单个元素值数据类型
     * 默认值代表和{@link #singleType()}一样的类型
     */
    NumType singleValType() default NumType.Default;

    /**
     * 每个数组元素在读取后、应该skip的byte长度
     */
    int singleSkip() default 0;

    /**
     * 值处理表达式、针对于数组中每个值
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
     * 字节序模式
     */
    ByteOrder singleOrder() default ByteOrder.Default;

}
