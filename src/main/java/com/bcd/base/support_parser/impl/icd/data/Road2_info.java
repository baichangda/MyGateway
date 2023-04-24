package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.*;

import java.util.List;

public class Road2_info {
    @F_integer(len = 4)
    public long road_id;
    @F_integer(len = 1)
    public RoadType road_type;
    @F_float_integer(len = 4,valExpr = "x/10000000")
    public double road_lon;
    @F_float_integer(len = 4,valExpr = "x/10000000")
    public double road_lat;
    @F_integer(len = 4)
    public long road_alt;
    @F_integer(len = 4,var = 'a')
    public long lane_count;

    @F_skip(len = 64,mode = SkipMode.ReservedFromStart)
    public byte[] reserved;
    @F_bean_list(listLenExpr = "a")
    public List<Road2_info_lane> lane_info_array;


}
