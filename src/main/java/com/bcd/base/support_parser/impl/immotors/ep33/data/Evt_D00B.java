package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00B extends Evt_4_x {
    @F_num(type = NumType.uint8, var = 'a')
    public short BMSCellVolSumNum;
    @F_bean_list(listLenExpr = "a", passBitBuf = true)
    public Evt_D00B_BMSCellVol[] BMSCellVols;

    public static class Evt_D00B_BMSCellVol {
        @F_bit_num(len = 13, valType = NumType.float32, valExpr = "x*0.001")
        public float BMSCellVol;
        @F_bit_num(len = 1, valType = NumType.uint8, bitRemainingMode = BitRemainingMode.ignore)
        public byte BMSCellVolV;
    }

    public static void main(String[] args) {
        System.out.println(4.031 / 0.001);
    }
}


