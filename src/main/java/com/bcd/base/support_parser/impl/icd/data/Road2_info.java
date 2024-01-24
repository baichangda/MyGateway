package com.bcd.base.support_parser.impl.icd.data;


import com.bcd.base.support_parser.anno.*;

import java.util.List;

public class Road2_info {
    @F_num(type = NumType.uint32)
    public long road_id;
    @F_num(type = NumType.uint8)
    public RoadType road_type;
    @F_num(type = NumType.uint32,  valExpr = "x/10000000")
    public double road_lon;
    @F_num(type = NumType.uint32,  valExpr = "x/10000000")
    public double road_lat;
    @F_num(type = NumType.uint32)
    public long road_alt;
    @F_num(type = NumType.uint32, var = 'a')
    @F_skip(lenAfter = 43)
    public long lane_count;
    @F_bean_list(listLenExpr = "a")
    public List<Road2_info_lane> lane_info_array;


}
