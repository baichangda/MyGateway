package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.F_num_array;
import com.bcd.share.support_parser.anno.NumType;

public class DeleteRectangleArea implements PacketBody{
    //区域数
    @F_num(type = NumType.uint8, var = 'n')
    public short num;
    //区域id
    @F_num_array(singleType = NumType.uint32, lenExpr = "n")
    public long[] ids;
}
