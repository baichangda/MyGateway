package com.bcd.base.support_parser.anno;

import com.bcd.base.support_parser.builder.BuilderContext;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于任何字段
 * 用户自己实现解析逻辑
 * <p>
 * {@link #builderClass()}和{@link #processorClass()} 二选一
 *
 * 反解析中
 * 值可以为null、null的含义由定制逻辑自己实现
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface F_customize {
    /**
     * 处理类
     * 与{@link #builderClass()}互斥
     * 必须是{@link Processor}子类
     */
    Class<?> processorClass() default void.class;

    /**
     * asm构建类
     * 与{@link #processorClass()}互斥
     * 必须是{@link com.bcd.base.support_parser.builder.FieldBuilder}子类
     */
    Class<?> builderClass() default void.class;

    /**
     * 变量名称
     * 标注此标记的会在解析时候将值缓存,供表达式使用
     * 例如: m,n,a
     */
    char var() default '0';

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
