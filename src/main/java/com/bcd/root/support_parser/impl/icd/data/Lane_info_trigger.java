package com.bcd.root.support_parser.impl.icd.data;

import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.F_skip;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.anno.SkipMode;

public class Lane_info_trigger {
    @F_num(type = NumType.uint32)
    public long track_id;
    @F_num(type = NumType.uint8)
    public short lane_id;
    @F_num(type = NumType.uint16)
    public int lane_dis;
    @F_num(type = NumType.uint16)
    public int speed;
    @F_num(type = NumType.uint8)
    public short status;
    @F_skip(mode = SkipMode.reservedFromStart, len = 16)
    public byte reserved;
}
