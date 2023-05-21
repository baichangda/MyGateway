package com.bcd.base.support_parser.impl.immotors.ep33.data;


import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0005 extends Evt_2_6 {
    @F_bit_num(len = 28, valExpr = "x*0.000001")
    public double Latitude;
    @F_bit_num(len = 4)
    public byte VehTyp;
    @F_bit_num(len = 16, valExpr = "x*0.01")
    public float GNSSDirection;
}
