package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_FFFF extends Evt_2_6 {
    @F_bit_num(len = 48, valType = NumType.uint64)
    public long EvtCRC;
}
