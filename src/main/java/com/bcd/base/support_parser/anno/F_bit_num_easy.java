package com.bcd.base.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 位解析
 * 最高支持连续32位
 *
 * 相邻的此注解字段会视为一组、也可以通过{@link #end()}在连续的注解字段中分多个组
 * 同一组的字段定义必须按照{@link #bitStart()}由高到低定义
 * 根据 bit组总长度 来读取对应长度字节
 * 1-8 1
 * 8-16 2
 * 17-24 3
 * 25-32 4
 *
 * 对比{@link F_bit_num}而言
 * 这个更轻量、生成的代码效率更高
 * 优先推荐使用此注解
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_bit_num_easy {
    /**
     * bit开始、bit高位
     * 包含
     * 例如3字节、bit最高位为23、bit最低位为0
     */
    int bitStart();

    /**
     * bit结束、bit低位
     * 包含
     * 例如3字节、bit最高位为23、bit最低位为0
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
