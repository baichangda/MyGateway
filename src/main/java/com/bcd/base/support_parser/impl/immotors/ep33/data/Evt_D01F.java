package com.bcd.base.support_parser.impl.immotors.ep33.data;


import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D01F extends Evt_4_x {
    @F_num(type = NumType.uint8)
    public short NetRecvRsn;
    @F_num(type = NumType.uint8)
    public short NetRecvActn;
    @F_bit_num(len = 48)
    public long NetRecvActnTimstmp;
    @F_num(type = NumType.uint8)
    public short NetRecvActnCnt;
    @F_num(type = NumType.uint8)
    public short NetRecvActnRst;
    @F_bit_num(len = 48)
    public long NetRecvtime;
}
