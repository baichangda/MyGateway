package com.bcd.share.support_parser.impl.immotors.ep33.data;

import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.impl.immotors.Evt_2_6;

public class Evt_000C extends Evt_2_6 {
    @F_bit_num(len = 4)
    public byte PotclVer;
    @F_bit_num(len = 4)
    public byte PotclSecyVer;
    @F_bit_num(len = 8, valExpr = "x+2000")
    public short CalendarYear;
    @F_bit_num(len = 5)
    public byte CalendarDay;
    @F_bit_num(len = 5)
    public byte CalendarMonth;
}
