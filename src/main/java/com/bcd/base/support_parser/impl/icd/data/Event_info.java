package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.*;

import java.util.List;

public class Event_info {
    @F_num(len = 2)
    public int event_id;
    @F_num(len = 2)
    public EventType event_type;
    @F_float_ieee754(type = FloatType_ieee754.Float64)
    public double event_timestamp;
    @F_num(len = 8, valExpr = "x/10000000")
    public double event_lon;
    @F_num(len = 8, valExpr = "x/10000000")
    public double event_lat;
    @F_num(len = 4)
    public long event_alt;
    @F_num(len = 4)
    public long event_road_id;
    @F_num(len = 2, var = 'a')
    public int src_count;
    @F_num(len = 2, var = 'b')
    public int target_count;

    @F_skip(len = 64, mode = SkipMode.ReservedFromStart)
    public byte[] reserved;
    @F_num_array(lenExpr = "a", singleLen = 4)
    public long[] src_array;
    @F_bean_list(listLenExpr = "b")
    public List<Event_target> event_target_array;
}
