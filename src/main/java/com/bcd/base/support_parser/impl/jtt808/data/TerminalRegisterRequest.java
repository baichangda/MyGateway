package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.impl.jtt808.processor.TerminalRegister_plateNo_processor;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class TerminalRegisterRequest implements PacketBody {
    //省域id
    @F_num(type = NumType.uint16)
    public int provinceId;
    //市县域id
    @F_num(type = NumType.uint16)
    public int cityId;
    //制造商id
    @F_num_array(singleType = NumType.uint8, len = 11)
    public byte[] manufacturerId;
    //终端型号
    @F_num_array(singleType = NumType.uint8, len = 30)
    public byte[] terminalType;
    //终端id
    @F_num_array(singleType = NumType.uint8, len = 30)
    public byte[] terminalId;
    //车牌颜色
    @F_num(type = NumType.uint8)
    public short plateColor;
    //车牌
    @F_customize(processorClass = TerminalRegister_plateNo_processor.class)
    public String plateNo;

    public static void main(String[] args) {
        Parser.enableGenerateClassFile();
        Parser.enablePrintBuildLog();
        Processor<Packet> processor = Parser.getProcessor(Packet.class);

        TerminalRegisterRequest terminalRegisterRequest = new TerminalRegisterRequest();
        terminalRegisterRequest.provinceId = 1;
        terminalRegisterRequest.cityId = 1;
        terminalRegisterRequest.manufacturerId = new byte[11];
        terminalRegisterRequest.terminalType = new byte[30];
        terminalRegisterRequest.terminalId = new byte[30];
        terminalRegisterRequest.plateColor = 100;
        terminalRegisterRequest.plateNo = "啊啊啊";

        PacketHeader packetHeader = new PacketHeader();
        packetHeader.msgId = 0x0100;
        packetHeader.msgLen = 76+ terminalRegisterRequest.plateNo.getBytes(Charset.forName("GBK")).length;
        packetHeader.msgSn = 1;

        Packet packet = new Packet();
        packet.startFlag = 0x7e;
        packet.header = packetHeader;
        packet.body = terminalRegisterRequest;
        packet.code = 0;
        packet.endFlag = 0x7e;

        ByteBuf buffer = Unpooled.buffer();
        processor.deProcess(buffer,null,packet);
        System.out.println(ByteBufUtil.hexDump(buffer));


        Packet process = processor.process(buffer, null);
        System.out.println(process);
    }
}
