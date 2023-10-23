package com.bcd.share.support_parser.impl.icd.data;

import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;

public class Road2_info_lane_area_point {
    @F_num(type = NumType.uint16)
    public int area_point_id;
    @F_num(type = NumType.uint32,  valExpr = "x/10000000")
    public double area_point_lon;
    @F_num(type = NumType.uint32,  valExpr = "x/10000000")
    public double area_point_lat;
    @F_num(type = NumType.uint32)
    public long area_point_alt;
}
