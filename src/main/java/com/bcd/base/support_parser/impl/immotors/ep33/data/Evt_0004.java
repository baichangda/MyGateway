package com.bcd.base.support_parser.impl.immotors.ep33.data;


import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0004 extends Evt_2_6 {
    @F_num(type = NumType.uint16,  valExpr = "x*0.1-500")
    public float GnssAlt;
    @F_bit_num(len = 29,unsigned = false,  valExpr = "x*0.000001")
    public double Longitude;
    @F_bit_num(len = 2)
    public byte GPSSts;


}
