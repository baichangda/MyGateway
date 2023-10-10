package com.bcd.root.support_parser.impl.icd.data;

import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.F_skip;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.anno.SkipMode;

public class Road_info_lane_target {
    @F_num(type = NumType.uint32)
    public long track_id;
    @F_num(type = NumType.uint32)
    public long lane_dis;
    @F_num(type = NumType.uint32)
    public long lane_v;

    @F_skip(mode = SkipMode.reservedFromStart, len = 32)
    public byte reserved;
}
