package com.bcd.root.support_parser.impl.jtt808.data;

import com.bcd.root.support_parser.anno.F_bean_list;
import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.NumType;

public class QueryTerminalParamResponse implements PacketBody {
    //应答流水号
    @F_num(type = NumType.uint16)
    public int sn;
    //应答参数个数
    @F_num(type = NumType.uint8, var = 'n')
    public short num;
    //参数项列表
    @F_bean_list(listLenExpr = "n")
    public TerminalParamItem[] items;
}
