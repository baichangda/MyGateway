package com.bcd.root.support_parser.impl.immotors.ep33.data;

import com.bcd.root.support_parser.anno.BitRemainingMode;
import com.bcd.root.support_parser.anno.F_bit_num;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.impl.immotors.Evt_2_6;

public class Evt_0007 extends Evt_2_6 {
    @F_bit_num(len = 14, valType = NumType.float64, bitRemainingMode = BitRemainingMode.ignore, unsigned = false, valExpr = "x*0.0009765625")
    public double AcceX;
    @F_bit_num(len = 14, valType = NumType.float64, bitRemainingMode = BitRemainingMode.ignore, unsigned = false, valExpr = "x*0.0009765625")
    public double AcceY;
    @F_bit_num(len = 14, valType = NumType.float64, bitRemainingMode = BitRemainingMode.ignore, unsigned = false, valExpr = "x*0.0009765625")
    public double AcceZ;
}
