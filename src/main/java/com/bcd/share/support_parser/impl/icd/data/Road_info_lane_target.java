package com.bcd.share.support_parser.impl.icd.data;

import com.bcd.share.support_parser.anno.C_skip;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.F_skip;
import com.bcd.share.support_parser.anno.NumType;
@C_skip(len = 32)
public class Road_info_lane_target {
    @F_num(type = NumType.uint32)
    public long track_id;
    @F_num(type = NumType.uint32)
    public long lane_dis;
    @F_num(type = NumType.uint32)
    public long lane_v;

}
