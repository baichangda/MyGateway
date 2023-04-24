package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.SkipMode;

public class Road_info_lane_target {
    @F_integer(len = 4)
    public long track_id;
    @F_integer(len = 4)
    public long lane_dis;
    @F_integer(len = 4)
    public long lane_v;

    @F_skip(mode = SkipMode.ReservedFromStart,len = 32)
    public byte[] reserved;
}
