package com.bcd.share.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于bcd编码、即8421码解析
 * 在此编码下、按照4个bit位转换为1字节进行解析、首先得到一个byte数组
 * 此后byte数组可以转换为string
 *
 * 支持如下2种类型
 * 1、byte[]
 * 2、string
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_bcd {
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


    /**
     * 字段类型为字符串类型才有效
     * 在写入时候
     * 当字符串长度小于指定长度时候、需要填充0
     * 填充模式如下
     * {@link StringAppendMode#noAppend} 不补
     * {@link StringAppendMode#lowAddressAppend} 低内存地址补
     * {@link StringAppendMode#highAddressAppend} 高内存地址补
     * <p>
     * 内存地址解释
     * 假如有byte[4]、其中0是低内存地址、3是高内存地址
     */
    StringAppendMode appendMode() default StringAppendMode.noAppend;
}
