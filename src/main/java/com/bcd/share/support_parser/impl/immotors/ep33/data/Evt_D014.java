package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.impl.immotors.Evt_4_x;

public class Evt_D014 extends Evt_4_x {
    @F_bit_num(len = 56)
    public long DTCInfomationICM;
    @F_bit_num(len = 56)
    public long DTCInfomationCARLog;
    @F_bit_num(len = 56)
    public long DTCInfomationIMATE;
    @F_bit_num(len = 56)
    public long DTCInfomationAMP;
    @F_bit_num(len = 56)
    public long DTCInfomationPGM;
}
