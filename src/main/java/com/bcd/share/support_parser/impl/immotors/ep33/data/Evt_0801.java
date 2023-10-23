package com.bcd.share.support_parser.impl.immotors.ep33.data;

import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.F_skip;
import com.bcd.share.support_parser.anno.NumType;
import com.bcd.share.support_parser.impl.immotors.Evt_2_6;

public class Evt_0801 extends Evt_2_6 {
    @F_skip(len = 5)
    public byte skip;
    @F_bit_num(len = 8,  valExpr = "x*0.392157")
    public float BrkPdlPos;
}
