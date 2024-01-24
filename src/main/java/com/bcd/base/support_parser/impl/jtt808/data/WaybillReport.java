package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_num_array;
import com.bcd.base.support_parser.anno.NumType;

public class WaybillReport implements PacketBody{
    @F_num(type = NumType.uint32, var = 'n')
    public long len;
    @F_num_array(singleType = NumType.uint8, lenExpr = "n")
    public byte[] content;
}
