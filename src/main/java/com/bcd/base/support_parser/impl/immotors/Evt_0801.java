package com.bcd.base.support_parser.impl.immotors;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0801 extends Evt_2_6 {
    @F_skip(lenBefore = 5)
    @F_num(type = NumType.uint8,  valExpr = "x*0.392157")
    public float BrkPdlPos;
}
