package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_customize;
import com.bcd.base.support_parser.impl.immotors.Evt;
import com.bcd.base.support_parser.impl.immotors.ep33.processor.Packet_evts_processor;

import java.util.List;

public class Packet {
    @F_customize(processorClass = Packet_evts_processor.class)
    public List<Evt> evts;
}
