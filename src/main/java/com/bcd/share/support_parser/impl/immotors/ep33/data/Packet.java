package com.bcd.share.support_parser.impl.immotors.ep33.data;

import com.bcd.share.support_parser.anno.F_customize;
import com.bcd.share.support_parser.impl.immotors.Evt;
import com.bcd.share.support_parser.impl.immotors.ep33.processor.Packet_evts_processor;

import java.util.List;

public class Packet {
    @F_customize(processorClass = Packet_evts_processor.class)
    public Evt[] evts;
}
