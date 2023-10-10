package com.bcd.share.support_parser.impl.icd.data;

import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.F_skip;
import com.bcd.share.support_parser.anno.NumType;
import com.bcd.share.support_parser.anno.SkipMode;

public class Sensor_body_lidar implements Sensor_body {
    @F_num(type = NumType.uint32)
    public long distance;
    @F_num(type = NumType.uint16)
    public int line_count;
    @F_num(type = NumType.uint16, valType = NumType.float32, valExpr = "x/100")
    public float hori_view_angle;
    @F_num(type = NumType.uint16, valType = NumType.float32, valExpr = "x/100")
    public float vert_view_angle;

    @F_skip(mode = SkipMode.reservedFromStart, len = 32)
    public byte reserved;
}
