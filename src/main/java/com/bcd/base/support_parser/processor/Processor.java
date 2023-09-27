package com.bcd.base.support_parser.processor;

import com.bcd.base.exception.BaseRuntimeException;
import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.BitOrder;
import com.bcd.base.support_parser.anno.ByteOrder;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;

public interface Processor<T> {
    T process(final ByteBuf data, final ProcessContext<?> parentContext);

     void deProcess(final ByteBuf data, final ProcessContext<?> parentContext, T instance);


}
