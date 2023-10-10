package com.bcd.root.support_parser.impl.immotors.ep33.data;

import com.bcd.root.support_parser.anno.F_bit_num;
import com.bcd.root.support_parser.anno.F_skip;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.impl.immotors.Evt_2_6;

public class Evt_000A extends Evt_2_6 {
    @F_bit_num(len = 8, valType = NumType.int8)
    public byte cellSignalStrength;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte cellRAT;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte cellRATadd;
    @F_bit_num(len = 9, valType = NumType.uint16)
    public short cellChanID;
    @F_bit_num(len = 8, valType = NumType.uint8)
    public short GNSSSATS;

    @F_skip(len = 2)
    public byte skip;
}
