package com.bcd.base.support_parser.anno;

import com.bcd.base.support_parser.builder.BuilderContext;
import com.bcd.base.support_parser.processor.ProcessContext;
import io.netty.buffer.ByteBuf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于实体类集合字段、支持如下类型
 * T[] 数组
 * List<T> 集合、默认实例是ArrayList类型
 * {@link #listLen()}和{@link #listLenExpr()} 二选一、代表字段所占用总字节数
 * <p>
 * 反解析中
 * 值可以为null、此时代表空集合
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_bean_list {

    /**
     * 对象集合
     * 与{@link #listLenExpr()}互斥
     */
    int listLen() default 0;

    /**
     * 对象集合长度表达式
     * 用于对象集合字段不定长度的解析,配合var参数使用,代表的是当前集合元素的个数
     * 适用于 List<TestBean> 字段类型
     * 与{@link #listLen()}互斥
     * 例如:
     * m
     * m*n
     */
    String listLenExpr() default "";

    /**
     * 是否传递bitBuf对象到bean的解析中
     * 会在父类中构造bitBuf对象并设置
     * {@link com.bcd.base.support_parser.processor.ProcessContext#bitBuf_reader}
     * {@link com.bcd.base.support_parser.processor.ProcessContext#bitBuf_writer}
     * 集合中的子类获取bitBuf逻辑参考
     * {@link BuilderContext#getVarNameBitBuf_reader()}
     * {@link BuilderContext#getVarNameBitBuf_writer()}
     */
    boolean passBitBuf() default false;
}
