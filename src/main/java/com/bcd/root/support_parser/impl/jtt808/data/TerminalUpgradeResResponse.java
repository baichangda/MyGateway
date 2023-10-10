package com.bcd.root.support_parser.impl.jtt808.data;

import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.NumType;

public class TerminalUpgradeResResponse implements PacketBody {
    //升级类型
    @F_num(type = NumType.uint8)
    public byte type;
    //升级结果
    @F_num(type = NumType.uint8)
    public byte res;
}
