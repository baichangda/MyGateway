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

    public final BitBuf_reader getBitBuf_reader(ByteBuf byteBuf) {
        if (bitBuf_reader == null) {
            bitBuf_reader = BitBuf_reader.newBitBuf(byteBuf);
        }
        return bitBuf_reader;
    }

    public final BitBuf_writer getBitBuf_writer(ByteBuf byteBuf) {
        if (bitBuf_writer == null) {
            bitBuf_writer = BitBuf_writer.newBitBuf(byteBuf);
        }
        return bitBuf_writer;
    }
}
