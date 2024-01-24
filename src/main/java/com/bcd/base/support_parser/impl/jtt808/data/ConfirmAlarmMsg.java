package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

public class ConfirmAlarmMsg implements PacketBody {
    //报警消息流水号
    @F_num(type = NumType.uint16)
    public int sn;
    //人工确认报警类型
    @F_num(type = NumType.uint32)
    public int type;
}
