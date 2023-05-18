package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_000A extends Evt_2_6 {
    @F_integer(bit = 8)
    public byte cellSignalStrength;
    @F_integer(bit = 3)
    public byte cellRAT;
    @F_integer(bit = 3)
    public byte cellRATadd;
    @F_integer(bit = 9)
    public short cellChanID;
    @F_integer(bit = 8)
    public short GNSSSATS;

    @F_skip(len = 2)
    public int skip;
}
