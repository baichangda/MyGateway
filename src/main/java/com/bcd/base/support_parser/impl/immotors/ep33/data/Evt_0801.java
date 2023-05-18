package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0801 extends Evt_2_6 {
    @F_skip(len = 5)
    public int skip;
    @F_integer(bit = 8)
    public short BrkPdlPos;
}
