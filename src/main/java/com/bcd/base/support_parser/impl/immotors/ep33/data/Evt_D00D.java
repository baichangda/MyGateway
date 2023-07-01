package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00D extends Evt_4_x {
    @F_num(type = NumType.uint8, var = 'a')
    public short BMSBusbarTemSumNum;
    @F_bean_list(listLenExpr = "a", passBitBuf = true)
    public Evt_D00D_BMSBusbarTem[] BMSBusbarTems;

    public static class Evt_D00D_BMSBusbarTem {
        @F_bit_num(len = 8, valType = NumType.float32, valExpr = "x-40")
        public float BMSBusbarTem;
        @F_bit_num(len = 1,valType = NumType.uint8, bitRemainingMode = BitRemainingMode.ignore)
        public byte BMSBusbarTemV;
    }
}
