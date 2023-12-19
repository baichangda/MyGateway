package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.impl.immotors.Evt_4_x;

public class Evt_D012 extends Evt_4_x {
    @F_bit_num(len = 56)
    public long DTCInfomationSDM;
    @F_bit_num(len = 56)
    public long DTCInfomationIBS;
    @F_bit_num(len = 56)
    public long DTCInfomationEPS;
    @F_bit_num(len = 56)
    public long DTCInfomationEPS_S;
    @F_bit_num(len = 56)
    public long DTCInfomationSCM;
    @F_bit_num(len = 56)
    public long DTCInfomationRBM;
    @F_bit_num(len = 56)
    public long DTCInfomationSAS;
    @F_bit_num(len = 56)
    public long DTCInfomationRWSGW;
    @F_bit_num(len = 56)
    public long DTCInfomationRWS;
}
