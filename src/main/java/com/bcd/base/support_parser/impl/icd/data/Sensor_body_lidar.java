package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.SkipMode;

public class Sensor_body_lidar implements Sensor_body{
    @F_integer(len = 4)
    public long distance;
    @F_integer(len = 2)
    public int line_count;
    @F_integer(len = 2,valExpr = "x/100")
    public float hori_view_angle;
    @F_integer(len = 2,valExpr = "x/100")
    public float vert_view_angle;

    @F_skip(mode = SkipMode.ReservedFromStart,len = 32)
    public byte[] reserved;
}
