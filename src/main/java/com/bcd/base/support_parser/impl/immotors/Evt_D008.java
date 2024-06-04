package com.bcd.base.support_parser.impl.immotors;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D008 extends Evt_4_x {
    @F_num(type = NumType.uint56)
    public long DTCInfomationBMS;
    @F_num(type = NumType.uint56)
    public long DTCInfomationECM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationEPB;
    @F_num(type = NumType.uint56)
    public long DTCInfomationPLCM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationTCM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationTPMS;
    @F_num(type = NumType.uint56)
    public long DTCInfomationTC;
    @F_num(type = NumType.uint56)
    public long DTCInfomationISC;
    @F_num(type = NumType.uint56)
    public long DTCInfomationSAC;
    @F_num(type = NumType.uint56)
    public long DTCInfomationIMCU;
}
