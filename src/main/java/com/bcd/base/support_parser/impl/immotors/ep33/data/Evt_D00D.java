package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_integer_array;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00D extends Evt_4_x {
    @F_integer(len = 1, var = 'a')
    public short BMSBusbarTemSumNum;
    @F_integer_array(lenExpr = "a", bit = 8, valExpr = "x-40")
    public short[] BMSBusbarTem;
    @F_integer_array(lenExpr = "a", bit = 1)
    public byte[] BMSBusbarTemV;
}
