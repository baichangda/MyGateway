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
public @interface F_bit_num_array {
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
     * 单个元素bit长度
     */
    int singleLen();

    /**
     * 每个数组元素在读取bit后、应该skip的bit长度
     */
    int singleSkip() default 0;

    /**
     * bit位表示的值是否为无符号类型
     * 当是有符号类型时候
     * bit最高位为符号位、0代表正数、1代表负数
     * 对值的求解方式为
     * 正数、正常进行求值
     * 负数、所有bit位取反+1、求值后、代表负数
     */
    boolean unsigned() default true;

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
     * 表示当前字段bit解析结束时候、剩余多余的bit(不满1字节的)的处理模式
     */
    BitRemainingMode bitRemainingMode() default BitRemainingMode.Default;


}
