package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.*;
import com.bcd.share.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00C extends Evt_4_x {
    @F_num(type = NumType.uint8, var = 'a')
    public short BMSCellTemSumNum;
    @F_bean_list(listLenExpr = "a")
    public Evt_D00C_BMSCellTem[] BMSCellTems;

    public static class Evt_D00C_BMSCellTem {
        @F_bit_num(len = 8,  valExpr = "x-40")
        public float BMSCellTem;
        @F_bit_num(len = 1, bitRemainingMode = BitRemainingMode.ignore)
        public byte BMSCellTemV;
    }
}
