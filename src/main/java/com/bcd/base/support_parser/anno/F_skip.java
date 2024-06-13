package com.bcd.base.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于任何字段
 * 跳过数个字节
 * 可以配合其他注解一起使用
 * 用在字段上面只是为了占位、解析不会对字段进行赋值、反解析也不会使用字段值
 *
 * 注意:
 * 如果和{@link F_bit_num}、{@link F_bit_num_array}一起配合使用、需要保证skip之前的bit位已经finish、否则会导致解析出错
 * 因为{@link F_skip}会导致
 * {@link com.bcd.base.support_parser.util.BitBuf_reader}
 * {@link com.bcd.base.support_parser.util.BitBuf_writer}
 * 中的byteBuf变化、但是不会改变b、bitOffset、这样会导致后续解析出错
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_skip {
    /**
     * 解析前跳过字节
     * 和{@link #lenExprBefore()}互斥
     */
    int lenBefore() default 0;

    /**
     * 解析前跳过字节表达式
     * 和{@link #lenBefore()}互斥
     * 变量取值来源于var、globalVar
     * 使用globalVar时候必须在变量前面带上@
     * 例如:
     * m
     * m*n
     * a*b-1
     * a*(b-2)
     * a*(b-2)+@a
     */
    String lenExprBefore() default "";


    /**
     * 解析后跳过字节
     * 和{@link #lenExprAfter()}互斥
     */
    int lenAfter() default 0;

    /**
     * 解析前跳过字节表达式
     * 和{@link #lenAfter()}互斥
     * 变量取值来源于var、globalVar
     * 使用globalVar时候必须在变量前面带上@
     * 例如:
     * m
     * m*n
     * a*b-1
     * a*(b-2)
     * a*(b-2)+@a
     */
    String lenExprAfter() default "";
}
