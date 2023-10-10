package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;

public class TempPositionFollow implements PacketBody {
    //时间间隔
    @F_num(type = NumType.uint16)
    public int interval;
    //位置跟踪有效期
    @F_num(type = NumType.uint32)
    public long valid;
}
