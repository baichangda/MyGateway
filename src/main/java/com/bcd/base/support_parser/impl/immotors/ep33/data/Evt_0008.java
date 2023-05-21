package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0008 extends Evt_2_6 {
    @F_bit_num(len = 16)
    public int cellMCC;
    @F_bit_num(len = 16)
    public int cellMNC;
    @F_skip(len = 2)
    public int skip;
}
