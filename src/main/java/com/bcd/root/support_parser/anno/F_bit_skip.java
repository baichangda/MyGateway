package com.bcd.root.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_bit_skip {
    /**
     * 占用bit位
     */
    int len();


    /**
     * 表示当前字段bit解析结束时候、剩余多余的bit(不满1字节的)的处理模式
     */
    BitRemainingMode bitRemainingMode() default BitRemainingMode.Default;
}
