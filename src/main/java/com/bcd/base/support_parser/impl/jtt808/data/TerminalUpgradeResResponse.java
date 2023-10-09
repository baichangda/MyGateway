package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

public class TerminalUpgradeResResponse implements PacketBody{
    //升级类型
    @F_num(type = NumType.uint8)
    public byte type;
    //升级结果
    @F_num(type = NumType.uint8)
    public byte res;
}
