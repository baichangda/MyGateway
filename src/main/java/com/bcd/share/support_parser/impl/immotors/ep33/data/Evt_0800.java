package com.bcd.share.support_parser.impl.immotors.ep33.data;

import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.F_skip;
import com.bcd.share.support_parser.impl.immotors.Evt_2_6;

public class Evt_0800 extends Evt_2_6 {
    @F_bit_num(len = 2)
    public byte SysPwrMd;
    @F_bit_num(len = 1)
    public byte SysPwrMdV;
    @F_bit_num(len = 1)
    public byte SysVolV;
    @F_bit_num(len = 4)
    public byte TrShftLvrPos;
    @F_bit_num(len = 8, valExpr = "x*0.1+3")
    public float SysVol;
    @F_skip(len = 3)
    public byte skip;
    @F_bit_num(len = 1)
    public byte TrShftLvrPosV;
}
