package com.bcd.base.support_parser.impl.immotors;


import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D011 extends Evt_4_x {
    @F_num(type = NumType.uint56)
    public long DTCInfomationEPMCU;
    @F_num(type = NumType.uint56)
    public long DTCInfomationWLC;
    @F_num(type = NumType.uint56)
    public long DTCInfomationSCU;
    @F_num(type = NumType.uint56)
    public long DTCInfomationEOPC;
    @F_num(type = NumType.uint56)
    public long DTCInfomationCCU;
}
