package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_num_array;
import com.bcd.base.support_parser.anno.F_string;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00E extends Evt_4_x {
    @F_num(type = NumType.uint8, var = 'a')
    public short BMSRptBatCodeNum;
    @F_string(lenExpr = "a")
    public String BMSRptBatCodeAsc;
}
