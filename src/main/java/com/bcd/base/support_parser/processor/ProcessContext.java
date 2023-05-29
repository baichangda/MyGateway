package com.bcd.base.support_parser.processor;


import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;
import io.netty.buffer.ByteBuf;

public class ProcessContext<T> {
    public T instance;
    public final ProcessContext parentContext;

    //bit字段传递
    public BitBuf_reader bitBuf_reader;
    public BitBuf_writer bitBuf_writer;

    public ProcessContext(T instance, ProcessContext parentContext) {
        this.instance = instance;
        this.parentContext = parentContext;
    }

    public final static BitBuf_reader getBitBuf_reader(ByteBuf byteBuf, ProcessContext parentContext) {
        if (parentContext == null || parentContext.bitBuf_reader == null) {
            return BitBuf_reader.newBitBuf(byteBuf);
        } else {
            return parentContext.bitBuf_reader;
        }
    }

    public final static BitBuf_writer getBitBuf_writer(ByteBuf byteBuf, ProcessContext parentContext) {
        if (parentContext == null || parentContext.bitBuf_writer == null) {
            return BitBuf_writer.newBitBuf(byteBuf);
        } else {
            return parentContext.bitBuf_writer;
        }
    }

}
