package com.bcd.share.support_parser.impl.immotors.ep33.data;

import com.bcd.share.support_parser.anno.*;
import com.bcd.share.support_parser.impl.immotors.Evt_2_6;

public class Evt_0008 extends Evt_2_6 {
    @F_bit_num(len = 16)
    public int cellMCC;
    @F_bit_num(len = 16)
    public int cellMNC;
    @F_bit_num(len = 10)
    public short millisecond;
    @F_bit_num(len = 1)
    public byte spistatus;
}
