package com.bcd.base.support_parser.anno;

import com.bcd.base.support_parser.processor.ProcessContext;
import io.netty.buffer.ByteBuf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于实体类字段
 *
 * 反解析中
 * 值不能为null
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_bean {
    /**
     * 是否传递bitBuf对象到bean的解析中
     * 会在父类中构造bitBuf对象并设置
     * {@link com.bcd.base.support_parser.processor.ProcessContext#bitBuf_reader}
     * {@link com.bcd.base.support_parser.processor.ProcessContext#bitBuf_writer}
     * 集合中的子类获取bitBuf逻辑参考
     * {@link com.bcd.base.support_parser.processor.ProcessContext#getBitBuf_reader(ByteBuf, ProcessContext)}
     * {@link com.bcd.base.support_parser.processor.ProcessContext#getBitBuf_writer(ByteBuf, ProcessContext)}
     */
    boolean passBitBuf() default false;
}
