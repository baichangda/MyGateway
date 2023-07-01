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
    @F_num_array(len = 2, singleType = NumType.uint8)
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


    public static void main(String[] args) {
        Packet packet = new Packet();
        packet.vin = "abcd";
        packet.flag = PacketFlag.vehicle_login_data;
        packet.header = new byte[]{0x23, 0x23};
        final VehicleLoginData vehicleLoginData = new VehicleLoginData();
        vehicleLoginData.collectTime = new Date();
//        vehicleLoginData.iccid="8888";
        vehicleLoginData.subSystemNum = 2;
        vehicleLoginData.systemCodeLen = 0;
//        vehicleLoginData.systemCode="";
        packet.data = vehicleLoginData;
        Parser.withDefaultLogCollector_deParse();
        Parser.withDefaultLogCollector_parse();
        Parser.enableGenerateClassFile();
        ByteBuf byteBuf = Unpooled.buffer();
        Parser.deParse(packet, byteBuf, null);
        final ByteBuf byteBuf1 = byteBuf.copy();
        final Packet parse = Parser.parse(Packet.class, byteBuf, null);
        ByteBuf byteBuf2 = Unpooled.buffer();
        Parser.deParse(parse, byteBuf2, null);
        final String s1 = ByteBufUtil.hexDump(byteBuf1).toUpperCase();
        final String s2 = ByteBufUtil.hexDump(byteBuf2).toUpperCase();
        System.out.println(s1);
        System.out.println(s2);
        assert s1.equals(s2);
    }
}
