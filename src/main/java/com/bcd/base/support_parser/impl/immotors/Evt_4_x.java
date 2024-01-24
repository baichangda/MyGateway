package com.bcd.base.support_parser.impl.immotors;

import com.bcd.base.support_parser.anno.C_skip;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

@C_skip(lenExpr = "4+z")
public class Evt_4_x extends Evt {
    @F_num(type = NumType.uint16, var = 'z')
    public int evtLen;
}
