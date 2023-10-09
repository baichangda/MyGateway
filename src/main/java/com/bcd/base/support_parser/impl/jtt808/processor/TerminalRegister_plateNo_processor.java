package com.bcd.base.support_parser.impl.jtt808.processor;

import com.bcd.base.support_parser.impl.jtt808.data.Packet;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class TerminalRegister_plateNo_processor implements Processor<String> {
    static final Charset gbk = Charset.forName("GBK");

    @Override
    public String process(ByteBuf data, ProcessContext<?> parentContext) {
        Packet packet = (Packet) parentContext.parentContext.instance;
        int msgLen = packet.header.msgLen;
        return data.readCharSequence(msgLen - 76, gbk).toString();
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext<?> parentContext, String instance) {
        data.writeCharSequence(instance, gbk);
    }
}
