package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.*;

import java.util.List;

public class Msg_body_lane_detect_info implements Msg_body {
    @F_num(type = NumType.uint32)
    public long frame_id;
    public double frame_timestamp;
    @F_num(type = NumType.uint16, var = 'a')
    public int src_count;
    @F_num(type = NumType.uint32, var = 'b')
    @F_skip(lenAfter = 22)
    public long road_count;
    @F_num_array(lenExpr = "a", singleType = NumType.uint32)
    public long[] src_array;
    @F_bean_list(listLenExpr = "b")
    public List<Road_info> road_info_array;
}
