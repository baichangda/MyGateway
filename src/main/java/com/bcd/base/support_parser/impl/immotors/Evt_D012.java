package com.bcd.base.support_parser.impl.immotors;


import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D012 extends Evt_4_x {
    @F_num(type = NumType.uint56)
    public long DTCInfomationSDM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationIBS;
    @F_num(type = NumType.uint56)
    public long DTCInfomationEPS;
    @F_num(type = NumType.uint56)
    public long DTCInfomationEPS_S;
    @F_num(type = NumType.uint56)
    public long DTCInfomationSCM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationRBM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationSAS;
    @F_num(type = NumType.uint56)
    public long DTCInfomationRWSGW;
    @F_num(type = NumType.uint56)
    public long DTCInfomationRWS;
}
