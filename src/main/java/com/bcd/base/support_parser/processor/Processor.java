package com.bcd.base.support_parser.processor;

import com.bcd.base.support_parser.exception.BaseRuntimeException;
import io.netty.buffer.ByteBuf;

public interface Processor<T> {

    T process(final ByteBuf data, final ProcessContext parentContext);

    default void deProcess(final ByteBuf data, final ProcessContext parentContext, T instance) {
        throw BaseRuntimeException.getException("not support");
    }
}
