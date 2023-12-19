package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.F_string;
import com.bcd.share.support_parser.impl.immotors.Evt_4_x;

public class Evt_D01F extends Evt_4_x {
    @F_bit_num(len = 8)
    public short NetRecvRsn;
    @F_bit_num(len = 8)
    public short NetRecvActn;
    @F_bit_num(len = 48)
    public long NetRecvActnTimstmp;
    @F_bit_num(len = 8)
    public short NetRecvActnCnt;
    @F_bit_num(len = 8)
    public short NetRecvActnRst;
    @F_bit_num(len = 48)
    public long NetRecvtime;
}
