package com.bcd.base.support_parser.impl.immotors;


import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0802 extends Evt_2_6 {
    @F_bit_num(len = 15,  valExpr = "x*0.015625")
    public float VehSpdAvgDrvn;
    @F_bit_num(len = 1)
    public byte VehSpdAvgDrvnV;
}
