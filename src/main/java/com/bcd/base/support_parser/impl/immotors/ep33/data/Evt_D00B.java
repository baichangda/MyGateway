package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_float_integer_array;
import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_string;
import com.bcd.base.support_parser.anno.StringAppendMode;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00B extends Evt_4_x {
    @F_integer(len = 1)
    public short BMSCellVolSumNum;
    @F_float_integer_array(singleLen = 0)
    public float[] BMSCellVol;
}
