package com.bcd.base.support_parser.impl.icd.data;


import com.bcd.base.support_parser.anno.*;

import java.util.List;

public class Event_info {
    @F_num(type = NumType.uint16)
    public int event_id;
    @F_num(type = NumType.uint16)
    public EventType event_type;
    public double event_timestamp;
    @F_num(type = NumType.int64,  valExpr = "x/10000000")
    public double event_lon;
    @F_num(type = NumType.int64,  valExpr = "x/10000000")
    public double event_lat;
    @F_num(type = NumType.uint32)
    public long event_alt;
    @F_num(type = NumType.uint32)
    public long event_road_id;
    @F_num(type = NumType.uint16, var = 'a')
    public int src_count;
    @F_num(type = NumType.uint16, var = 'b')
    @F_skip(lenAfter = 32)
    public int target_count;
    @F_num_array(lenExpr = "a", singleType = NumType.uint32)
    public long[] src_array;
    @F_bean_list(listLenExpr = "b")
    public List<Event_target> event_target_array;
}
