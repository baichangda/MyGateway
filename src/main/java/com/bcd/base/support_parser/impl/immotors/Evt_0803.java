package com.bcd.base.support_parser.impl.immotors;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0803 extends Evt_2_6 {
    @F_bit_num(len = 24)
    public int VehOdo;
    @F_bit_num(len = 1)
    public byte VehOdoV;
    @F_bit_num(len = 1)
    public byte BrkPdlPosV;
}
