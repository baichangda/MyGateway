package com.bcd.share.support_parser.impl.jtt808.processor;

import com.bcd.share.exception.BaseRuntimeException;
import com.bcd.share.support_parser.impl.jtt808.data.PacketHeader;
import com.bcd.share.support_parser.impl.jtt808.data.SubPacket;
import com.bcd.share.support_parser.processor.ProcessContext;
import com.bcd.share.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;

public class SubPacketProcessor implements Processor<SubPacket> {
    @Override
    public SubPacket process(ByteBuf data, ProcessContext parentContext) {
        PacketHeader packetHeader = (PacketHeader) parentContext.instance;
        if (packetHeader.subPacketFlag == 0) {
            return null;
        } else {
            SubPacket subPacket = new SubPacket();
            subPacket.total = data.readUnsignedShort();
            subPacket.no = data.readUnsignedShort();
            return subPacket;
        }
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext parentContext, SubPacket instance) {
        PacketHeader packetHeader = (PacketHeader) parentContext.instance;
        if (packetHeader.subPacketFlag == 1) {
            if (instance == null) {
                throw BaseRuntimeException.getException("subPacketFlag[1] but subPacket is null");
            }
            data.writeShort(instance.total);
            data.writeShort(instance.no);
        }
    }
}
