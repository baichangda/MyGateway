package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.SkipMode;

public class Sensor_body_camera implements Sensor_body {
    @F_num(len = 2)
    public int pixel;
    @F_num(len = 2)
    public int focal;
    @F_num(len = 2,valExpr = "x/100")
    public float hori_view_angle;
    @F_num(len = 2,valExpr = "x/100")
    public float vert_view_angle;

    @F_skip(mode = SkipMode.ReservedFromStart,len = 32)
    public byte[] reserved;
}
