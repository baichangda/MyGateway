package com.bcd.base.support_parser.impl.immotors.ep33.processor;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.icd.data.*;
import com.bcd.base.support_parser.impl.immotors.Evt;
import com.bcd.base.support_parser.impl.immotors.ep33.data.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;


public class Packet_evts_processor implements Processor<List<Evt>> {
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
                default -> {
                    throw BaseRuntimeException.getException("evtId[{}] not support", evtId);
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
