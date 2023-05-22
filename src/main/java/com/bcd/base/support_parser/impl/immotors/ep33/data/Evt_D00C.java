package com.bcd.base.support_parser.impl.immotors.ep33.data;


import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00C extends Evt_4_x {
    @F_num(len = 1, var = 'a')
    public short BMSCellTemSumNum;
    @F_bean_list(listLenExpr = "a", passBitBuf = true)
    public Evt_D00C_BMSCellTem[] BMSCellTems;

    public static class Evt_D00C_BMSCellTem {
        @F_bit_num(len = 8, valExpr = "x-40")
        public double BMSCellTem;
        @F_bit_num(len = 1, bitRemainingMode = BitRemainingMode.Ignore)
        public byte BMSCellTemV;
    }
}
