package com.bcd.share.support_parser.impl.jtt808.data;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class TerminalRegisterResponse implements PacketBody {
    //应答流水号
    public int sn;
    //结果
    public byte res;
    //鉴权码
    public String code;


    static final Charset gbk = Charset.forName("GBK");
    public static TerminalRegisterResponse read(ByteBuf data, int len){
        TerminalRegisterResponse terminalRegisterResponse = new TerminalRegisterResponse();
        terminalRegisterResponse.sn = data.readUnsignedShort();
        terminalRegisterResponse.res = data.readByte();
        terminalRegisterResponse.code = data.readCharSequence(len - 3, gbk).toString();
        return terminalRegisterResponse;
    }
}
