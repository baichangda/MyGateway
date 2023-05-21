package com.bcd.base.support_parser.impl.someip.data;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_num_array;

public class Packet {
    @F_num(len = 2)
    public int serviceId;

    @F_bit_num(len = 1)
    public byte flag;

    @F_bit_num(len = 15)
    public short methodIdOrEventId;

    @F_num(len = 4,var = 'a')
    public long length;

    @F_num(len = 2)
    public int clientId;

    @F_num(len = 2)
    public int sessionId;

    @F_num(len = 1)
    public short protocolVersion;

    @F_num(len = 1)
    public short interfaceVersion;

    @F_num(len = 1)
    public MessageType messageType;

    @F_num(len = 1)
    public ReturnCode returnCode;

    @F_num_array(lenExpr = "a-8",singleLen = 1)
    public byte[] payload;
}
