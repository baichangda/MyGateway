package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.impl.gb32960.processor.PacketDataFieldProcessor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.util.Date;


public class Packet {
    //头 0-2
    @F_num_array(len = 2, singleType = NumType.int8)
    public byte[] header;
    //命令标识 2-3
    @F_num(type = NumType.uint8)
    public PacketFlag flag;
    //应答标识 3-4
    @F_num(type = NumType.uint8)
    public short replyFlag;
    //唯一识别码 4-21
    @F_string(len = 17)
    public String vin;
    //数据单元加密方式 21-22
    @F_num(type = NumType.uint8)
    public short encodeWay;
    //数据单元长度 22-24
    @F_num(type = NumType.uint16, var = 'a')
    public int contentLength;
    //数据单元
//    @F_integer_array(lenExpr = "a", singleLen = 1)
    public byte[] dataContent;
    @F_customize(
            processorClass = PacketDataFieldProcessor.class
//            builderClass = PacketDataFieldBuilder.class
    )
//    @F_skip(lenExpr = "a")
    public PacketData data;
    //异或校验位
    @F_num(type = NumType.int8)
    public byte code;

    public static byte[] response(byte[] src) {
        byte[] dest = new byte[25];
        System.arraycopy(src,0,dest,0,3);
        dest[3] = 0x01;
        System.arraycopy(src,4,dest,4,18);
        byte xor = 0;
        for (int i = 0; i < 24; i++) {
            xor ^= src[i];
        }
        dest[24] = xor;
        return dest;
    }
}
