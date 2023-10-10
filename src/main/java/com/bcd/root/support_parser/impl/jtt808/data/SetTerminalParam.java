package com.bcd.root.support_parser.impl.jtt808.data;

import com.bcd.root.support_parser.anno.F_bean_list;
import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.NumType;

public class SetTerminalParam implements PacketBody {
    //参数总数
    @F_num(type = NumType.uint8, var = 'n')
    public short total;
    //参数项列表
    @F_bean_list(listLenExpr = "n")
    public TerminalParamItem[] items;
}
