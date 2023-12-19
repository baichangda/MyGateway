package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.impl.immotors.Evt_4_x;

public class Evt_D010 extends Evt_4_x {
    @F_bit_num(len = 56)
    public long DTCInfomationIAM;
    @F_bit_num(len = 56)
    public long DTCInfomationIPD;
    @F_bit_num(len = 56)
    public long DTCInfomationIECU;
    @F_bit_num(len = 56)
    public long DTCInfomationFDR;
    @F_bit_num(len = 56)
    public long DTCInfomationLFSDA;
    @F_bit_num(len = 56)
    public long DTCInfomationRFSDA;
    @F_bit_num(len = 56)
    public long DTCInfomationLHRDA;
    @F_bit_num(len = 56)
    public long DTCInfomationRHRDA;
}
