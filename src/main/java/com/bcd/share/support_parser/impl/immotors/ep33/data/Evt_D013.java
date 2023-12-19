package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.impl.immotors.Evt_4_x;

public class Evt_D013 extends Evt_4_x {
    @F_bit_num(len = 56)
    public long DTCInfomationDCM_FL;
    @F_bit_num(len = 56)
    public long DTCInfomationDCM_FR;
    @F_bit_num(len = 56)
    public long DTCInfomationDCM_RL;
    @F_bit_num(len = 56)
    public long DTCInfomationDCM_RR;
    @F_bit_num(len = 56)
    public long DTCInfomationATC;
    @F_bit_num(len = 56)
    public long DTCInfomationAMR;
    @F_bit_num(len = 56)
    public long DTCInfomationBPEPS;
    @F_bit_num(len = 56)
    public long DTCInfomationMSM_Drv;
    @F_bit_num(len = 56)
    public long DTCInfomationMSM_Psng;
    @F_bit_num(len = 56)
    public long DTCInfomationDLP;
    @F_bit_num(len = 56)
    public long DTCInfomationBCM;
}
