package com.bcd.base.support_parser.impl.immotors.ep33.processor;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.exception.BaseRuntimeException;
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

    final Processor<Evt_0001> processor_Evt_0001 = Parser.getProcessor(Evt_0001.class);
    final Processor<Evt_0004> processor_Evt_0004 = Parser.getProcessor(Evt_0004.class);
    final Processor<Evt_0005> processor_Evt_0005 = Parser.getProcessor(Evt_0005.class);
    final Processor<Evt_0006> processor_Evt_0006 = Parser.getProcessor(Evt_0006.class);
    final Processor<Evt_0007> processor_Evt_0007 = Parser.getProcessor(Evt_0007.class);
    final Processor<Evt_0008> processor_Evt_0008 = Parser.getProcessor(Evt_0008.class);
    final Processor<Evt_0009> processor_Evt_0009 = Parser.getProcessor(Evt_0009.class);
    final Processor<Evt_000A> processor_Evt_000A = Parser.getProcessor(Evt_000A.class);
    final Processor<Evt_0800> processor_Evt_0800 = Parser.getProcessor(Evt_0800.class);
    final Processor<Evt_0801> processor_Evt_0801 = Parser.getProcessor(Evt_0801.class);
    final Processor<Evt_0802> processor_Evt_0802 = Parser.getProcessor(Evt_0802.class);
    final Processor<Evt_0803> processor_Evt_0803 = Parser.getProcessor(Evt_0803.class);
    final Processor<Evt_D006> processor_Evt_D006 = Parser.getProcessor(Evt_D006.class);
    final Processor<Evt_D008> processor_Evt_D008 = Parser.getProcessor(Evt_D008.class);
    final Processor<Evt_D009> processor_Evt_D009 = Parser.getProcessor(Evt_D009.class);
    final Processor<Evt_D00A> processor_Evt_D00A = Parser.getProcessor(Evt_D00A.class);
    final Processor<Evt_D00B> processor_Evt_D00B = Parser.getProcessor(Evt_D00B.class);
    final Processor<Evt_D00C> processor_Evt_D00C = Parser.getProcessor(Evt_D00C.class);
    final Processor<Evt_D00D> processor_Evt_D00D = Parser.getProcessor(Evt_D00D.class);
    final Processor<Evt_D00E> processor_Evt_D00E = Parser.getProcessor(Evt_D00E.class);
    final Processor<Evt_D00F> processor_Evt_D00F = Parser.getProcessor(Evt_D00F.class);
    final Processor<Evt_2_6_unknown> processor_Evt_2_6_unknown = Parser.getProcessor(Evt_2_6_unknown.class);
    final Processor<Evt_4_x_unknown> processor_Evt_4_x_unknown = Parser.getProcessor(Evt_4_x_unknown.class);

    @Override
    public List<Evt> process(ByteBuf data, ProcessContext parentContext) {
        final List<Evt> evts = new ArrayList<>();
        while (data.isReadable()) {
            final int evtId = data.getUnsignedShort(data.readerIndex());
            final Evt evt;
            switch (evtId) {
                case 0x0001 -> {
                    evt = processor_Evt_0001.process(data, parentContext);
                }
                case 0x0004 -> {
                    evt = processor_Evt_0004.process(data, parentContext);
                }
                case 0x0005 -> {
                    evt = processor_Evt_0005.process(data, parentContext);
                }
                case 0x0006 -> {
                    evt = processor_Evt_0006.process(data, parentContext);
                }
                case 0x0007 -> {
                    evt = processor_Evt_0007.process(data, parentContext);
                }
                case 0x0008 -> {
                    evt = processor_Evt_0008.process(data, parentContext);
                }
                case 0x0009 -> {
                    evt = processor_Evt_0009.process(data, parentContext);
                }
                case 0x000A -> {
                    evt = processor_Evt_000A.process(data, parentContext);
                }
                case 0x0800 -> {
                    evt = processor_Evt_0800.process(data, parentContext);
                }
                case 0x0801 -> {
                    evt = processor_Evt_0801.process(data, parentContext);
                }
                case 0x0802 -> {
                    evt = processor_Evt_0802.process(data, parentContext);
                }
                case 0x0803 -> {
                    evt = processor_Evt_0803.process(data, parentContext);
                }
                case 0xD006 -> {
                    evt = processor_Evt_D006.process(data, parentContext);
                }
                case 0xD008 -> {
                    evt = processor_Evt_D008.process(data, parentContext);
                }
                case 0xD009 -> {
                    evt = processor_Evt_D009.process(data, parentContext);
                }
                case 0xD00A -> {
                    evt = processor_Evt_D00A.process(data, parentContext);
                }
                case 0xD00B -> {
                    evt = processor_Evt_D00B.process(data, parentContext);
                }
                case 0xD00C -> {
                    evt = processor_Evt_D00C.process(data, parentContext);
                }
                case 0xD00D -> {
                    evt = processor_Evt_D00D.process(data, parentContext);
                }
                case 0xD00E -> {
                    evt = processor_Evt_D00E.process(data, parentContext);
                }
                case 0xD00F -> {
                    evt = processor_Evt_D00F.process(data, parentContext);
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
                        evt = processor_Evt_2_6_unknown.process(data, parentContext);
//                        final String evtIdHex = ByteBufUtil.hexDump(new byte[]{(byte) (evtId>>8), (byte) evtId});
//                        logger.warn("evtId[{}] 2+6 not support,skip[8]", evtIdHex);
                    } else if (evtId >= 0xD000 && evtId <= 0xDFFF) {
                        evt = processor_Evt_4_x_unknown.process(data, parentContext);
//                        final int len = ((Evt_4_x) evt).evtLen;
//                        final String evtIdHex = ByteBufUtil.hexDump(new byte[]{(byte) (evtId>>8), (byte) evtId});
//                        logger.warn("evtId[{}] 4+X not support,skip[{}]", evtIdHex, 4 + len);
                    } else {
                        final String evtIdHex = ByteBufUtil.hexDump(new byte[]{(byte) (evtId >> 8), (byte) evtId});
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
            final int evtId = evt.evtId;
            switch (evtId) {
                case 0x0001 -> {
                    processor_Evt_0001.deProcess(data, parentContext, (Evt_0001) evt);
                }
                case 0x0004 -> {
                    processor_Evt_0004.deProcess(data, parentContext, (Evt_0004) evt);
                }
                case 0x0005 -> {
                    processor_Evt_0005.deProcess(data, parentContext, (Evt_0005) evt);
                }
                case 0x0006 -> {
                    processor_Evt_0006.deProcess(data, parentContext, (Evt_0006) evt);
                }
                case 0x0007 -> {
                    processor_Evt_0007.deProcess(data, parentContext, (Evt_0007) evt);
                }
                case 0x0008 -> {
                    processor_Evt_0008.deProcess(data, parentContext, (Evt_0008) evt);
                }
                case 0x0009 -> {
                    processor_Evt_0009.deProcess(data, parentContext, (Evt_0009) evt);
                }
                case 0x000A -> {
                    processor_Evt_000A.deProcess(data, parentContext, (Evt_000A) evt);
                }
                case 0x0800 -> {
                    processor_Evt_0800.deProcess(data, parentContext, (Evt_0800) evt);
                }
                case 0x0801 -> {
                    processor_Evt_0801.deProcess(data, parentContext, (Evt_0801) evt);
                }
                case 0x0802 -> {
                    processor_Evt_0802.deProcess(data, parentContext, (Evt_0802) evt);
                }
                case 0x0803 -> {
                    processor_Evt_0803.deProcess(data, parentContext, (Evt_0803) evt);
                }
                case 0xD006 -> {
                    processor_Evt_D006.deProcess(data, parentContext, (Evt_D006) evt);
                }
                case 0xD008 -> {
                    processor_Evt_D008.deProcess(data, parentContext, (Evt_D008) evt);
                }
                case 0xD009 -> {
                    processor_Evt_D009.deProcess(data, parentContext, (Evt_D009) evt);
                }
                case 0xD00A -> {
                    processor_Evt_D00A.deProcess(data, parentContext, (Evt_D00A) evt);
                }
                case 0xD00B -> {
                    processor_Evt_D00B.deProcess(data, parentContext, (Evt_D00B) evt);
                }
                case 0xD00C -> {
                    processor_Evt_D00C.deProcess(data, parentContext, (Evt_D00C) evt);
                }
                case 0xD00D -> {
                    processor_Evt_D00D.deProcess(data, parentContext, (Evt_D00D) evt);
                }
                case 0xD00E -> {
                    processor_Evt_D00E.deProcess(data, parentContext, (Evt_D00E) evt);
                }
                case 0xD00F -> {
                    processor_Evt_D00F.deProcess(data, parentContext, (Evt_D00F) evt);
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
                        processor_Evt_2_6_unknown.deProcess(data, parentContext, (Evt_2_6_unknown) evt);
//                        final String evtIdHex = ByteBufUtil.hexDump(new byte[]{(byte) (evtId>>8), (byte) evtId});
//                        logger.warn("evtId[{}] 2+6 not support,skip[8]", evtIdHex);
                    } else if (evtId >= 0xD000 && evtId <= 0xDFFF) {
                        processor_Evt_4_x_unknown.deProcess(data, parentContext, (Evt_4_x_unknown) evt);
//                        final int len = ((Evt_4_x) evt).evtLen;
//                        final String evtIdHex = ByteBufUtil.hexDump(new byte[]{(byte) (evtId>>8), (byte) evtId});
//                        logger.warn("evtId[{}] 4+X not support,skip[{}]", evtIdHex, 4 + len);
                    } else {
                        final String evtIdHex = ByteBufUtil.hexDump(new byte[]{(byte) (evtId >> 8), (byte) evtId});
                        throw BaseRuntimeException.getException("evtId[{}] not support", evtIdHex);
                    }
                }
            }
        }
    }

}
