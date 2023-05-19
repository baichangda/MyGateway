package com.bcd.base.support_parser.impl.immotors.ep33.processor;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.icd.data.*;
import com.bcd.base.support_parser.impl.immotors.Evt;
import com.bcd.base.support_parser.impl.immotors.ep33.data.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import com.google.common.base.Strings;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class Packet_evts_processor implements Processor<List<Evt>> {

    static Logger logger = LoggerFactory.getLogger(Packet_evts_processor.class);

    @Override
    public List<Evt> process(ByteBuf data, ProcessContext parentContext) {
        final List<Evt> evts = new ArrayList<>();
        while (data.isReadable()) {
            final int evtId = data.getUnsignedShort(data.readerIndex());
            final Evt evt;
            switch (evtId) {
                case 0x0001 -> {
                    evt = Parser.parse(Evt_0001.class, data, parentContext);
                }
                case 0x0004 -> {
                    evt = Parser.parse(Evt_0004.class, data, parentContext);
                }
                case 0x0005 -> {
                    evt = Parser.parse(Evt_0005.class, data, parentContext);
                }
                case 0x0006 -> {
                    evt = Parser.parse(Evt_0006.class, data, parentContext);
                }
                case 0x0007 -> {
                    evt = Parser.parse(Evt_0007.class, data, parentContext);
                }
                case 0x0008 -> {
                    evt = Parser.parse(Evt_0008.class, data, parentContext);
                }
                case 0x0009 -> {
                    evt = Parser.parse(Evt_0009.class, data, parentContext);
                }
                case 0x000A -> {
                    evt = Parser.parse(Evt_000A.class, data, parentContext);
                }
                case 0x0801 -> {
                    evt = Parser.parse(Evt_0801.class, data, parentContext);
                }
                case 0x0802 -> {
                    evt = Parser.parse(Evt_0802.class, data, parentContext);
                }
                case 0x0803 -> {
                    evt = Parser.parse(Evt_0803.class, data, parentContext);
                }
                case 0xD006 -> {
                    evt = Parser.parse(Evt_D006.class, data, parentContext);
                }
                default -> {
                    final String evtIdHex = Strings.padStart(Integer.toHexString(evtId).toUpperCase(), 4, '0');
                    if ((evtId >= 0x0001 && evtId <= 0x07FF)
                            || (evtId >= 0x0800 && evtId <= 0x0FFF)
                            || (evtId >= 0x1000 && evtId <= 0x2FFF)
                            || (evtId >= 0x3000 && evtId <= 0x4FFF)
                            || (evtId >= 0x5000 && evtId <= 0x5FFF)
                            || (evtId >= 0x6000 && evtId <= 0x6FFF)
                            || (evtId >= 0x7000 && evtId <= 0x8FFF)
                            || (evtId >= 0x9000 && evtId <= 0xAFFF)
                            || evtId == 0XFFFF
                    ) {
                        logger.warn("evtId[{}] 2+6 not support,skip[8]", evtIdHex);
                        data.skipBytes(8);
                    } else if (evtId >= 0xD000 && evtId <= 0xDFFF) {
                        data.skipBytes(2);
                        final int len = data.readUnsignedShort();
                        data.skipBytes(len);
                        logger.warn("evtId[{}] 4+X not support,skip[{}]", evtIdHex, 4 + len);
                    } else {
                        throw BaseRuntimeException.getException("evtId[{}] not support", evtIdHex);
                    }
                    continue;
                }
            }
            evts.add(evt);
        }
        return evts;
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext parentContext, List<Evt> instance) {
        Parser.deParse(instance, data, parentContext);
    }

    public static void main(String[] args) {
        System.out.println("[" + new String(new byte[1]) + "]");
    }
}
