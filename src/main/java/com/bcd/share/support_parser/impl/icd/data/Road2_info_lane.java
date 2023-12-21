package com.bcd.share.support_parser.impl.icd.data;


import com.bcd.share.support_parser.anno.*;

import java.util.List;

public class Road2_info_lane {
    @F_num(type = NumType.uint32)
    public long lane_id;
    @F_num(type = NumType.uint16,  valExpr = "x/100")
    public float lane_azimuth;
    @F_num(type = NumType.uint8)
    public LaneCanalization lane_canalization;
    @F_num(type = NumType.uint16, var = 'a')
    @F_skip(lenAfter = 23)
    public int area_point_count;
    @F_bean_list(listLenExpr = "a")
    public List<Road2_info_lane_area_point> area_point_array;
}
