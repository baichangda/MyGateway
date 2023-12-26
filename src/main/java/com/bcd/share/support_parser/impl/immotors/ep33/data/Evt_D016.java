package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;
import com.bcd.share.support_parser.impl.immotors.Evt_4_x;

public class Evt_D016 extends Evt_4_x {
    @F_num(type = NumType.uint56)
    public long DTCInfomationLHCMS;
    @F_num(type = NumType.uint56)
    public long DTCInfomationRHCMS;
    @F_num(type = NumType.uint56)
    public long DTCInfomationRLSM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationRRSM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationPMA;
    @F_num(type = NumType.uint56)
    public long DTCInfomationLVBM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationIMU;
}
