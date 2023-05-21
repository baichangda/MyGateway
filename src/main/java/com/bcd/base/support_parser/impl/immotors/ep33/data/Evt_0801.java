package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0801 extends Evt_2_6 {
    @F_skip(len = 5)
    public int skip;
    @F_bit_num(len = 8)
    public short BrkPdlPos;
}
