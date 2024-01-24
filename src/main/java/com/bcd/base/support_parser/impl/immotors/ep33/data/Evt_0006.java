package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0006 extends Evt_2_6 {
    @F_bit_num(len = 24,  valExpr = "x*0.1")
    public float HDop;
    @F_bit_num(len = 24,  valExpr = "x*0.1")
    public float VDop;
}
