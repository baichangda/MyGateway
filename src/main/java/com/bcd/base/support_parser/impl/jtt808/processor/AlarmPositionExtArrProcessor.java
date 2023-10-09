package com.bcd.base.support_parser.impl.jtt808.processor;

import com.bcd.base.support_parser.impl.jtt808.data.AlarmPositionExt;
import com.bcd.base.support_parser.impl.jtt808.data.Packet;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class AlarmPositionExtArrProcessor implements Processor<AlarmPositionExt[]> {
    static final Charset gbk = Charset.forName("GBK");

    @Override
    public AlarmPositionExt[] process(ByteBuf data, ProcessContext<?> parentContext) {
        Packet packet = (Packet) parentContext.parentContext.instance;
        int msgLen = packet.header.msgLen;
        int len = msgLen - 28;
        ArrayList<AlarmPositionExt> list = new ArrayList<>();
        while (len > 0) {
            AlarmPositionExt alarmPositionExt = new AlarmPositionExt();
            alarmPositionExt.id = data.readUnsignedByte();
            short tempLen = data.readUnsignedByte();
            alarmPositionExt.len = tempLen;
            byte[] temp = new byte[tempLen];
            data.readBytes(temp);
            alarmPositionExt.data = temp;
            len -= tempLen;
            list.add(alarmPositionExt);
        }
        if (list.isEmpty()) {
            return null;
        } else {
            return list.toArray(new AlarmPositionExt[0]);
        }
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext<?> parentContext, AlarmPositionExt[] instance) {
        if (instance != null && instance.length > 0) {
            for (AlarmPositionExt alarmPositionExt : instance) {
                data.writeByte(alarmPositionExt.id);
                data.writeByte(alarmPositionExt.len);
                data.writeBytes(alarmPositionExt.data);
            }
        }
    }
}
