package com.bcd.share.support_parser.processor;

import com.bcd.share.support_parser.anno.F_customize;
import io.netty.buffer.ByteBuf;

public interface Processor<T> {

    default T process(final ByteBuf data) {
        return process(data, new ProcessContext<>(data));
    }

    default void deProcess(final ByteBuf data, T instance) {
        deProcess(data, new ProcessContext<>(data), instance);
    }

    /**
     * @param data
     * @param parentContext 父解析上下文、主要用于{@link F_customize}获取父类bean
     *                      不能为null
     * @return
     */
    T process(final ByteBuf data, final ProcessContext<?> parentContext);

    /**
     * @param data
     * @param parentContext 父解析上下文、主要用于{@link F_customize}获取父类bean
     *                      不能为null
     * @param instance
     */
    void deProcess(final ByteBuf data, final ProcessContext<?> parentContext, T instance);
}
