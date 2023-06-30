package com.bcd.base.support_parser.impl.immotors.ep33.processor;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.immotors.Evt;
import com.bcd.base.support_parser.impl.immotors.ep33.data.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
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
                case 0x0800 -> {
                    evt = Parser.parse(Evt_0800.class, data, parentContext);
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
                case 0xD008 -> {
                    evt = Parser.parse(Evt_D008.class, data, parentContext);
                }
                case 0xD009 -> {
                    evt = Parser.parse(Evt_D009.class, data, parentContext);
                }
                case 0xD00A -> {
                    evt = Parser.parse(Evt_D00A.class, data, parentContext);
                }
                case 0xD00B -> {
                    evt = Parser.parse(Evt_D00B.class, data, parentContext);
                }
                case 0xD00C -> {
                    evt = Parser.parse(Evt_D00C.class, data, parentContext);
                }
                case 0xD00D -> {
                    evt = Parser.parse(Evt_D00D.class, data, parentContext);
                }
                case 0xD00E -> {
                    evt = Parser.parse(Evt_D00E.class, data, parentContext);
                }
                case 0xD00F -> {
                    evt = Parser.parse(Evt_D00F.class, data, parentContext);
                }
                default -> {
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
                        evt = Parser.parse(Evt_2_6_unknown.class, data, parentContext);
//                        final String evtIdHex = ByteBufUtil.hexDump(new byte[]{(byte) (evtId>>8), (byte) evtId});
//                        logger.warn("evtId[{}] 2+6 not support,skip[8]", evtIdHex);
                    } else if (evtId >= 0xD000 && evtId <= 0xDFFF) {
                        evt = Parser.parse(Evt_4_x_unknown.class, data, parentContext);
//                        final int len = ((Evt_4_x) evt).evtLen;
//                        final String evtIdHex = ByteBufUtil.hexDump(new byte[]{(byte) (evtId>>8), (byte) evtId});
//                        logger.warn("evtId[{}] 4+X not support,skip[{}]", evtIdHex, 4 + len);
                    } else {
                        final String evtIdHex = ByteBufUtil.hexDump(new byte[]{(byte) (evtId>>8), (byte) evtId});
                        throw BaseRuntimeException.getException("evtId[{}] not support", evtIdHex);
                    }
                }
            }
            evts.add(evt);
        }
        return evts;
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext parentContext, List<Evt> instance) {
        for (Evt evt : instance) {
            Parser.deParse(evt, data, parentContext);
        }
    }

}
