package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0803 extends Evt_2_6 {
    @F_integer(bit = 24)
    public int VehOdo;
    @F_integer(bit = 1)
    public byte VehOdoV;
    @F_integer(bit = 1)
    public byte BrkPdlPosV;

    @F_skip(len = 2)
    public int skip;
}
