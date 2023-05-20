package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_float_integer;
import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0005 extends Evt_2_6 {
    @F_float_integer(bit = 28, valExpr = "x*0.000001")
    public double Latitude;
    @F_integer(bit = 4)
    public byte VehTyp;
    @F_float_integer(bit = 16, valExpr = "x*0.01")
    public float GNSSDirection;
}
