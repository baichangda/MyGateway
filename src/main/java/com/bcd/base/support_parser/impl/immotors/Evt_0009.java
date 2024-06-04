package com.bcd.base.support_parser.impl.immotors;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0009 extends Evt_2_6 {
    @F_num(type = NumType.uint16)
    public int cellLAC;
    @F_num(type = NumType.uint32)
    public long CellID;
}
