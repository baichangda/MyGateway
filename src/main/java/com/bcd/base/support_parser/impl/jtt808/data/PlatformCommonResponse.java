package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

public class PlatformCommonResponse implements PacketBody{
    //应答流水号
    @F_num(type = NumType.uint16)
    public int sn;
    //应答id
    @F_num(type = NumType.uint16)
    public int id;
    @F_num(type = NumType.uint8)
    public byte res;
}
