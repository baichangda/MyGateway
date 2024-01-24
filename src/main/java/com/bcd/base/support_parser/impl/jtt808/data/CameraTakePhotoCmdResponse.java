package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_num_array;
import com.bcd.base.support_parser.anno.NumType;

public class CameraTakePhotoCmdResponse implements PacketBody {
    //应答流水号
    @F_num(type = NumType.uint16)
    public int sn;
    //拍摄命令
    @F_num(type = NumType.uint8)
    public byte res;
    //id个数
    @F_num(type = NumType.uint16, var = 'n')
    public int num;
    //id列表
    @F_num_array(singleType = NumType.uint32, lenExpr = "n")
    public long[] ids;
}
