package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_000E extends Evt_2_6 {
    @F_bit_num(len = 5)
    public byte BMSChrgSts;
    @F_bit_num(len = 10,valExpr = "x*0.1")
    public float BMSPackSOCBkup;
    @F_bit_num(len = 1)
    public byte BMSPackSOCVBkup;
    @F_num(type = NumType.uint8)
    public byte BMSOfbdChrgSpRsn;
    @F_num(type = NumType.uint8)
    public byte BMSWrlsChrgSpRsn;
}
