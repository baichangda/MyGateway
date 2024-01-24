package com.bcd.base.support_parser.impl.immotors.ep33.data;


import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D010 extends Evt_4_x {
    @F_num(type = NumType.uint56)
    public long DTCInfomationIAM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationIPD;
    @F_num(type = NumType.uint56)
    public long DTCInfomationIECU;
    @F_num(type = NumType.uint56)
    public long DTCInfomationFDR;
    @F_num(type = NumType.uint56)
    public long DTCInfomationLFSDA;
    @F_num(type = NumType.uint56)
    public long DTCInfomationRFSDA;
    @F_num(type = NumType.uint56)
    public long DTCInfomationLHRDA;
    @F_num(type = NumType.uint56)
    public long DTCInfomationRHRDA;
}
