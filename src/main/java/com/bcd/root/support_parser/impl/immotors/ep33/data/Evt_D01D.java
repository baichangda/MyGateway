package com.bcd.root.support_parser.impl.immotors.ep33.data;

import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.impl.immotors.Evt_4_x;

public class Evt_D01D extends Evt_4_x {
    @F_num(type = NumType.uint32)
    public long cellLAC5G;
    @F_num(type = NumType.uint64)
    public long CellID5G;
}
