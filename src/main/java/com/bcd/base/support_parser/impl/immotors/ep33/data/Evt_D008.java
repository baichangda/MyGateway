package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D008 extends Evt_4_x {
    @F_bit_num(len = 56)
    public long DTCInfomationBMS;
    @F_bit_num(len = 56)
    public long DTCInfomationECM;
    @F_bit_num(len = 56)
    public long DTCInfomationEPB;
    @F_bit_num(len = 56)
    public long DTCInfomationPLCM;
    @F_bit_num(len = 56)
    public long DTCInfomationTCM;
    @F_bit_num(len = 56)
    public long DTCInfomationTPMS;
    @F_bit_num(len = 56)
    public long DTCInfomationTC;
    @F_bit_num(len = 56)
    public long DTCInfomationISC;
    @F_bit_num(len = 56)
    public long DTCInfomationSAC;
    @F_bit_num(len = 56)
    public long DTCInfomationIMCU;
}
