package com.bcd.share.support_parser.impl.immotors.ep33.data;

import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.F_skip;
import com.bcd.share.support_parser.anno.NumType;
import com.bcd.share.support_parser.impl.immotors.Evt_2_6;

public class Evt_0803 extends Evt_2_6 {
    @F_bit_num(len = 24)
    public int VehOdo;
    @F_bit_num(len = 1)
    public byte VehOdoV;
    @F_bit_num(len = 1)
    public byte BrkPdlPosV;
    @F_skip(len = 2)
    public byte skip;
}
