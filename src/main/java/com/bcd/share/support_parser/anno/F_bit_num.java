package com.bcd.share.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用如下字段类型
 * byte、short、int、long、float、double、枚举类
 * <p>
 * 枚举类
 * 仅支持整型数字
 * 要求枚举类必有如下静态方法、例如
 * public enum Example{
 * public static Example fromInteger(int i){}
 * public int toInteger(){}
 * }
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_bit_num {
    /**
     * 占用bit位
     */
    int len();

    /**
     * 值数据类型
     * 对原始值进行{@link #valExpr()}运算后、存储最终值的类型
     * <p>
     * {@link #valExpr()} 不为空时
     * 此时定义的类型需要考虑存储如下值都不会出现溢出或错误
     * 原始值、运算过程中的值、结果值
     * <p>
     * 此属性对java解析程序没有影响、因为java程序在定义bean的field时候已经考虑进去了
     * 主要用于生成其他语言的解析程序时候用到、例如go
     */
    NumType valType();


    /**
     * 表示当前字段bit解析结束时候、剩余多余的bit(不满1字节的)的处理模式
     */
    BitRemainingMode bitRemainingMode() default BitRemainingMode.Default;

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
     * bit位表示的值是否为无符号类型
     * 当是有符号类型时候
     * bit最高位为符号位、0代表正数、1代表负数
     * 对值的求解方式为
     * 正数、正常进行求值
     * 负数、所有bit位取反+1、求值后、代表负数
     */
    boolean unsigned() default true;

    /**
     * bit位序模式
     */
    BitOrder order() default BitOrder.Default;

    /**
     * 变量名称
     * 标注此标记的会在解析时候将值缓存,供表达式使用
     * 例如: m,n,a
     */
    char var() default '0';
}
