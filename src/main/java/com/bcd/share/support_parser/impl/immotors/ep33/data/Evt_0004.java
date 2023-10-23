package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.NumType;
import com.bcd.share.support_parser.impl.immotors.Evt_2_6;

public class Evt_0004 extends Evt_2_6 {
    @F_bit_num(len = 16,  valExpr = "x*0.1-500")
    public double GnssAlt;

    @F_bit_num(len = 29,  valExpr = "x*0.000001")
    public double Longitude;

    @F_bit_num(len = 2)
    public byte GPSSts;
}
