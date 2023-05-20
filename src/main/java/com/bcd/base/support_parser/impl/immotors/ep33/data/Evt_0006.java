package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0006 extends Evt_2_6 {
    @F_integer(bit = 24, valExpr = "x*0.1")
    public double HDop;
    @F_integer(bit = 24, valExpr = "x*0.1")
    public double VDop;
}
