package com.bcd.base.support_parser.anno;

import com.bcd.base.support_parser.impl.gb32960.data.PacketFlag;
import com.bcd.base.support_parser.impl.gb32960.data.VehicleLoginData;
import com.bcd.base.support_parser.processor.Processor;

import java.lang.annotation.*;

/**
 * 用于和{@link F_bean}标注接口字段时候
 * 标注到其接口类型的实现类上
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface C_impl {
    /**
     * 用于配合{@link F_bean#implClassExpr()}使用
     * 当表达式解析为此值时候
     * 表明其实现类为当前注解标注的类
     *
     * 特殊值{@link Integer#MAX_VALUE}代表、当所有分支不命中时候、使用此注解标注的类
     */
    int[] value();

    /**
     * 处理类
     * 必须是{@link Processor}子类
     */
    Class<?> processorClass() default Void.class;

    /**
     * 处理类参数
     * 在new {@link #processorClass()}时候、会传入指定参数、以,分割
     * 空字符串、则不传入参数
     * 参数类型支持java类型有、int、float、double、String
     * 例如有3个参数
     * int、float、double、String
     * 则值可以是
     * "100,1.123F,100.123,\"test\""
     */
    String processorArgs() default "";
}
