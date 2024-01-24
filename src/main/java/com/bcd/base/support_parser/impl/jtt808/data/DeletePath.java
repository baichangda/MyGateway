package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_num_array;
import com.bcd.base.support_parser.anno.NumType;

public class DeletePath implements PacketBody{
    //路线数
    @F_num(type = NumType.uint8, var = 'n')
    public short num;
    //路线id
    @F_num_array(singleType = NumType.uint32, lenExpr = "n")
    public long[] ids;
}
