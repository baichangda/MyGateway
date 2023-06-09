package com.bcd.base.support_parser.impl.immotors.ep33.data;


import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0802 extends Evt_2_6 {
    @F_bit_num(len = 15, valType = NumType.float64, valExpr = "x*0.015625")
    public double VehSpdAvgDrvn;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte VehSpdAvgDrvnV;
    @F_skip(len = 4)
    public int skip;
}
