package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.F_string;
import com.bcd.share.support_parser.impl.immotors.Evt_4_x;

public class Evt_D01C extends Evt_4_x {
    @F_string(len = 18)
    public String IPAddress4;
    @F_string(len = 17)
    public String CurIMSI;
    @F_bit_num(len = 8)
    public short NetType;
    @F_bit_num(len = 16)
    public int rssi;
    @F_bit_num(len = 16)
    public int rsrp;
    @F_bit_num(len = 16)
    public int rscp;
    @F_bit_num(len = 16)
    public int sinr;
    @F_bit_num(len = 16)
    public int ecio;
}
