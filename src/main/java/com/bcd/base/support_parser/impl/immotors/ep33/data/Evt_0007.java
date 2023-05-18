package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_float_integer;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0007 extends Evt_2_6 {
    @F_float_integer(bit = 14, bitEnd = true, valExpr = "x*0.0009765625")
    public double AcceX;
    @F_float_integer(bit = 14, bitEnd = true, valExpr = "x*0.0009765625")
    public double AcceY;
    @F_float_integer(bit = 14, bitEnd = true, valExpr = "x*0.0009765625")
    public double AcceZ;
}
