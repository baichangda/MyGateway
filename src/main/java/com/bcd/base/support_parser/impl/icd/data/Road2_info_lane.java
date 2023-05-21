package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.*;

import java.util.List;

public class Road2_info_lane {
    @F_num(len = 4)
    public long lane_id;
    @F_num(len = 2, valExpr = "x/100")
    public float lane_azimuth;
    @F_num(len = 1)
    public LaneCanalization lane_canalization;
    @F_num(len = 2, var = 'a')
    public int area_point_count;

    @F_skip(len = 32, mode = SkipMode.ReservedFromStart)
    public byte[] reserved;
    @F_bean_list(listLenExpr = "a")
    public List<Road2_info_lane_area_point> area_point_array;
}
