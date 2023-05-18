package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_float_integer;
import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0004 extends Evt_2_6 {
    @F_float_integer(bit = 16,valExpr = "x*0.1-500")
    public float GnssAlt;

    @F_float_integer(bit = 29,valExpr = "x*0.000001")
    public double Longitude;

    @F_integer(bit = 2)
    public byte GPSSts;
}
