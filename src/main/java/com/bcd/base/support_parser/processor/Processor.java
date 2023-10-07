package com.bcd.base.support_parser.processor;

import io.netty.buffer.ByteBuf;

public interface Processor<T> {
    /**
     *
     * @param data
     * @param parentContext
     *        父解析上下文、主要用于{@link com.bcd.base.support_parser.anno.F_customize}获取父类bean
     *        如果类字段或者其子类字段都不包含此注解、则可以传null
     * @return
     */
    T process(final ByteBuf data, final ProcessContext<?> parentContext);

    /**
     *
     * @param data
     * @param parentContext
     *        父解析上下文、主要用于{@link com.bcd.base.support_parser.anno.F_customize}获取父类bean
     *        如果类字段或者其子类字段都不包含此注解、则可以传null
     * @param instance
     */
     void deProcess(final ByteBuf data, final ProcessContext<?> parentContext, T instance);
}
