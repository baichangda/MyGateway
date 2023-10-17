package com.bcd.share.support_parser.processor;

public class ProcessContext<T> {
    public final ProcessContext<?> parentContext;
    public T instance;
    public ProcessContext(T instance, ProcessContext<?> parentContext) {
        this.instance = instance;
        this.parentContext = parentContext;
    }

}
