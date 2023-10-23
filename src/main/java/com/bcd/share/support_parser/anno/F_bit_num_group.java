package com.bcd.share.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 最高支持连续32位
 *
 * 相邻的此注解字段会视为一组
 * 同一组字段会按照{@link #bitStart()}排序
 * 根据 bit组总长度 来读取对应长度字节
 * 1-8 1
 * 8-16 2
 * 17-24 3
 * 25-32 4
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_bit_num_group {
    /**
     * bit开始、从0开始、包含
     */
    int bitStart();

    /**
     * bit结束、不包含
     */
    int bitEnd();

    /**
     * 表示当前bit组结束
     */
    boolean end() default false;

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
     * 变量名称
     * 标注此标记的会在解析时候将值缓存,供表达式使用
     * 例如: m,n,a
     */
    char var() default '0';
}
