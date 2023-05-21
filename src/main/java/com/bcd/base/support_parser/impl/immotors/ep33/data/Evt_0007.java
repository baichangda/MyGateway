package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.BitRemainingMode;
import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0007 extends Evt_2_6 {
    @F_bit_num(len = 14, bitRemainingMode = BitRemainingMode.Ignore, unsigned = false, valExpr = "x*0.0009765625")
    public double AcceX;
    @F_bit_num(len = 14, bitRemainingMode = BitRemainingMode.Ignore, unsigned = false, valExpr = "x*0.0009765625")
    public double AcceY;
    @F_bit_num(len = 14, bitRemainingMode = BitRemainingMode.Ignore, unsigned = false, valExpr = "x*0.0009765625")
    public double AcceZ;
}
