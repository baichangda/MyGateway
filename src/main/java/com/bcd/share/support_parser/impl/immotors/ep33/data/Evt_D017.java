package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;
import com.bcd.share.support_parser.impl.immotors.Evt_4_x;

public class Evt_D017 extends Evt_4_x {
    @F_num(type = NumType.uint56)
    public long DTCInfomationIPS;
    @F_num(type = NumType.uint56)
    public long DTCInfomationRrDetnRdr;
    @F_num(type = NumType.uint56)
    public long DTCInfomationHUD;
    @F_num(type = NumType.uint56)
    public long DTCInfomationFLIDAR;
    @F_num(type = NumType.uint56)
    public long DTCInfomationFVCM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationSPD;
}
