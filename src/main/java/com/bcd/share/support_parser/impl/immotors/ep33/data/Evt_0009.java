package com.bcd.share.support_parser.impl.immotors.ep33.data;

import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.NumType;
import com.bcd.share.support_parser.impl.immotors.Evt_2_6;

public class Evt_0009 extends Evt_2_6 {
    @F_bit_num(len = 16, valType = NumType.uint16)
    public int cellLAC;
    @F_bit_num(len = 32, valType = NumType.uint32)
    public long CellID;
}
