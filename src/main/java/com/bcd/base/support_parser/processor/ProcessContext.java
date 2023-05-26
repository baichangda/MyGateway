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

    public final static BitBuf_reader getBitBuf_reader(ByteBuf byteBuf, ProcessContext context) {
        if (context == null) {
            return BitBuf_reader.newBitBuf(byteBuf);
        } else {
            if (context.bitBuf_reader == null) {
                context.bitBuf_reader = BitBuf_reader.newBitBuf(byteBuf);
            }
            return context.bitBuf_reader;
        }
    }

    public final static BitBuf_writer getBitBuf_writer(ByteBuf byteBuf, ProcessContext context) {
        if (context == null) {
            return BitBuf_writer.newBitBuf(byteBuf);
        } else {
            if (context.bitBuf_writer == null) {
                context.bitBuf_writer = BitBuf_writer.newBitBuf(byteBuf);
            }
            return context.bitBuf_writer;
        }
    }

}
