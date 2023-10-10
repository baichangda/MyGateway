package com.bcd.root.support_parser.impl.jtt808.data;

import com.bcd.root.support_parser.anno.F_bean;
import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.NumType;

public class QueryPositionResponse implements PacketBody {
    //应答流水号
    @F_num(type = NumType.uint16)
    public int sn;

    //位置信息
    @F_bean
    public Position position;
}
