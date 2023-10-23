package com.bcd.share.support_parser.impl.someip.data;

import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.F_num_array;
import com.bcd.share.support_parser.anno.NumType;

public class Packet {
    @F_num(type = NumType.uint16)
    public int serviceId;

    @F_bit_num(len = 1)
    public byte flag;

    @F_bit_num(len = 15)
    public short methodIdOrEventId;

    @F_num(type = NumType.uint32, var = 'a')
    public long length;

    @F_num(type = NumType.uint16)
    public int clientId;

    @F_num(type = NumType.uint16)
    public int sessionId;

    @F_num(type = NumType.uint8)
    public short protocolVersion;

    @F_num(type = NumType.uint8)
    public short interfaceVersion;

    @F_num(type = NumType.uint8)
    public MessageType messageType;

    @F_num(type = NumType.uint8)
    public ReturnCode returnCode;

    @F_num_array(lenExpr = "a-8", singleType = NumType.uint8)
    public byte[] payload;
}
