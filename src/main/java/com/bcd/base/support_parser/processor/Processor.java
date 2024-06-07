package com.bcd.base.support_parser.processor;

import com.bcd.base.support_parser.anno.F_customize;
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
     *                      具体指的是当前解析返回值赋值字段所在类的解析环境、其中{@link ProcessContext#instance}代表的是所在类的实例
     *                      不能为null
     *                      例如:
     *                      有如下类定义关系
     *                      class A{public B b}
     *                      class B{public C c}
     *                      class C{public D d}
     *                      class D{public int n}
     *                      那么当解析D.n字段时候、 {@link #process(ByteBuf, ProcessContext)}的parentContext代表类C的解析环境、可以有如下取值
     *                      parentContext.instance=c
     *                      parentContext.parentContext.instance=b
     *                      parentContext.parentContext.parentContext.instance=a
     *
     * @return
     */
    T process(final ByteBuf data, final ProcessContext<?> parentContext);

    /**
     * @param data
     * @param parentContext 和{{@link #process(ByteBuf)}}原理一致
     * @param instance
     */
    void deProcess(final ByteBuf data, final ProcessContext<?> parentContext, T instance);
}
