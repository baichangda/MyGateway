package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00B extends Evt_4_x {
    @F_num(len = 1, var = 'a')
    public short BMSCellVolSumNum;
    @F_bean_list(listLenExpr = "a", passBitBuf = true)
    public Evt_D00B_BMSCellVol[] BMSCellVols;

    public static class Evt_D00B_BMSCellVol {
        @F_bit_num(len = 13, valExpr = "x*0.001")
        public float BMSCellVol;
        @F_bit_num(len = 1, bitRemainingMode = BitRemainingMode.Ignore)
        public byte BMSCellVolV;
    }
}


