package com.bcd.share.support_parser.impl.immotors.ep33.data;

import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.impl.immotors.Evt_2_6;

public class Evt_000B extends Evt_2_6 {
    @F_bit_num(len = 8)
    public byte ModemStates;
    @F_bit_num(len = 1)
    public byte iNetworkSts;
    @F_bit_num(len = 16)
    public short iNetworkSts_ErrCode;
}