package com.bcd.base.support_parser.impl.immotors.ep33.data;


import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_bit_skip;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0005 extends Evt_2_6 {
    @F_bit_num(len = 28, valType = NumType.float64, valExpr = "x*0.000001")
    public double Latitude;
    @F_bit_skip(len = 4)
    public byte skip;
    @F_bit_num(len = 16, valType = NumType.float64, valExpr = "x*0.01")
    public double GNSSDirection;
}
