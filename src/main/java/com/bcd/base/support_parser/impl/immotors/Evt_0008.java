package com.bcd.base.support_parser.impl.immotors;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0008 extends Evt_2_6 {
    @F_num(type = NumType.uint16)
    public int cellMCC;
    @F_num(type = NumType.uint16)
    public int cellMNC;
    @F_bit_num(len = 10)
    public short millisecond;
    @F_bit_num(len = 1)
    public byte spistatus;
}
