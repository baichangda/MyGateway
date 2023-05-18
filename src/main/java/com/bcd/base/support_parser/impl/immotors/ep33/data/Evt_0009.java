package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0009 extends Evt_2_6 {
    @F_integer(bit = 16)
    public int cellLAC;
    @F_integer(bit = 32)
    public long CellID;
}
