package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_000F extends Evt_2_6 {
    @F_num(type = NumType.uint16, valExpr = "x*0.1-2000")
    public float TMActuToqHiPre;
    @F_bit_num(len = 15, valExpr = "x*0.1-1000")
    public float TMInvtrCrntHiPre;
}
