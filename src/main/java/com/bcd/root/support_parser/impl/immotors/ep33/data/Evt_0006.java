package com.bcd.root.support_parser.impl.immotors.ep33.data;

import com.bcd.root.support_parser.anno.F_bit_num;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.impl.immotors.Evt_2_6;

public class Evt_0006 extends Evt_2_6 {
    @F_bit_num(len = 24, valType = NumType.float64, valExpr = "x*0.1")
    public double HDop;
    @F_bit_num(len = 24, valType = NumType.float64, valExpr = "x*0.1")
    public double VDop;
}
