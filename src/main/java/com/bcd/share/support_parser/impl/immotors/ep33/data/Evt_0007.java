package com.bcd.share.support_parser.impl.immotors.ep33.data;

import com.bcd.share.support_parser.anno.BitRemainingMode;
import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.impl.immotors.Evt_2_6;

public class Evt_0007 extends Evt_2_6 {
    @F_bit_num(len = 14,  bitRemainingMode = BitRemainingMode.ignore, unsigned = false, valExpr = "x*0.0009765625")
    public float AcceX;
    @F_bit_num(len = 14,  bitRemainingMode = BitRemainingMode.ignore, unsigned = false, valExpr = "x*0.0009765625")
    public float AcceY;
    @F_bit_num(len = 14,  bitRemainingMode = BitRemainingMode.ignore, unsigned = false, valExpr = "x*0.0009765625")
    public float AcceZ;
}
