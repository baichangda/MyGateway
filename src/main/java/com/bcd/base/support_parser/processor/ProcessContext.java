package com.bcd.base.support_parser.processor;

import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_reader_log;
import com.bcd.base.support_parser.util.BitBuf_writer;
import com.bcd.base.support_parser.util.BitBuf_writer_log;
import io.netty.buffer.ByteBuf;

public class ProcessContext<T> {
    public T instance;
    public final ProcessContext<?> parentContext;
    public final ByteBuf byteBuf;
    public BitBuf_reader bitBuf_reader;
    public BitBuf_writer bitBuf_writer;
    public int[] globalVars;

    public ProcessContext(T instance, ProcessContext<?> parentContext) {
        this.instance = instance;
        this.parentContext = parentContext;
        this.byteBuf = parentContext.byteBuf;
        this.bitBuf_reader = parentContext.bitBuf_reader;
        this.bitBuf_writer = parentContext.bitBuf_writer;
        this.globalVars = parentContext.globalVars;
    }

    public ProcessContext(ByteBuf byteBuf) {
        this.instance = null;
        this.parentContext = null;
        this.byteBuf = byteBuf;
    }

    public final BitBuf_reader getBitBuf_reader() {
        if (bitBuf_reader == null) {
            bitBuf_reader = new BitBuf_reader(byteBuf);
        }
        return bitBuf_reader;
    }

    public final BitBuf_writer getBitBuf_writer() {
        if (bitBuf_writer == null) {
            bitBuf_writer = new BitBuf_writer(byteBuf);
        }
        return bitBuf_writer;
    }

    public final BitBuf_reader_log getBitBuf_reader_log() {
        if (bitBuf_reader == null) {
            bitBuf_reader = new BitBuf_reader_log(byteBuf);
        }
        return (BitBuf_reader_log) bitBuf_reader;
    }

    public final BitBuf_writer_log getBitBuf_writer_log() {
        if (bitBuf_writer == null) {
            bitBuf_writer = new BitBuf_writer_log(byteBuf);
        }
        return (BitBuf_writer_log) bitBuf_writer;
    }

    public void putGlobalVar(char c, int v) {
        if (globalVars == null) {
            globalVars = new int[52];
        }
        int i = c - 'A';
        if (i > 26) {
            i = c - 'a' + 26;
        }
        globalVars[i] = v;
    }

    public int getGlobalVar(char c) {
        int i = c - 'A';
        if (i > 26) {
            i = c - 'a' + 26;
        }
        return globalVars[i];
    }
}
