package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_num;

public class Road2_info_lane_area_point {
    @F_num(len = 2)
    public int area_point_id;
    @F_num(len = 4,valExpr = "x/10000000")
    public double area_point_lon;
    @F_num(len = 4,valExpr = "x/10000000")
    public double area_point_lat;
    @F_num(len = 4)
    public long area_point_alt;
}
