package com.bcd.base.support_parser.processor;


import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;

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
}
