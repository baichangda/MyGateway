package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0803 extends Evt_2_6 {
    @F_bit_num(len = 24, valType = NumType.uint32)
    public int VehOdo;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte VehOdoV;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BrkPdlPosV;

    @F_skip(len = 2)
    public int skip;
}
