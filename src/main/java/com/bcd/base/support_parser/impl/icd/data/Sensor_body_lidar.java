package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.C_skip;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

@C_skip(len = 32)
public class Sensor_body_lidar implements Sensor_body {
    @F_num(type = NumType.uint32)
    public long distance;
    @F_num(type = NumType.uint16)
    public int line_count;
    @F_num(type = NumType.uint16,  valExpr = "x/100")
    public float hori_view_angle;
    @F_num(type = NumType.uint16,  valExpr = "x/100")
    public float vert_view_angle;

}
