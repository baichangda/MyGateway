package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0001 extends Evt_2_6 {
    @F_bit_num(len = 48, valType = NumType.int64)
    public long TBOXSysTim;
}
