package com.bcd.root.support_parser.impl.jtt808.data;

import com.bcd.root.support_parser.anno.F_bean;
import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.NumType;

public class VehicleControlResponse implements PacketBody {
    @F_num(type = NumType.uint16)
    public int sn;
    @F_bean
    public Position position;
}
