package com.bcd.root.support_parser.impl.jtt808.data;


import com.bcd.root.support_parser.anno.*;
import com.bcd.root.support_parser.impl.jtt808.processor.SubPacketProcessor;

public class PacketHeader {
    //消息id
    @F_num(type = NumType.uint16)
    public int msgId;
    //消息体属性-保留
    @F_bit_skip(len = 1)
    public byte reserve;
    //消息体属性-版本标志
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte versionFlag;
    //消息体属性-分包
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte subPacketFlag;
    //消息体属性-数据加密方式
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte encryptType;
    //消息体属性-消息体长度
    @F_bit_num(len = 10, valType = NumType.uint32)
    public int msgLen;
    //协议版本号
    @F_num(type = NumType.uint8)
    public short version;
    //终端手机号
    @F_string(len = 10, appendMode = StringAppendMode.lowAddressAppend)
    public String phoneNumber;
    //消息流水号
    @F_num(type = NumType.uint16)
    public int msgSn;
    @F_customize(processorClass = SubPacketProcessor.class)
    public SubPacket subPacket;
}
