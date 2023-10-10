package com.bcd.root.support_parser.impl.immotors.ep33.data;

import com.bcd.root.support_parser.anno.F_bit_num_array;
import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00E extends Evt_4_x {
    @F_num(type = NumType.uint8, var = 'a')
    public short BMSRptBatCodeNum;
    @F_bit_num_array(lenExpr = "a", singleValType = NumType.uint8, singleLen = 8)
    public short[] BMSRptBatCodeAsc;
}
