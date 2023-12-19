package com.bcd.share.support_parser.impl.icd.data;

import com.bcd.share.support_parser.anno.C_skip;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.F_skip;
import com.bcd.share.support_parser.anno.NumType;

@C_skip(len = 32)
public class Sensor_body_millimeter_wave_radar implements Sensor_body {
    @F_num(type = NumType.int32)
    public int distance;
}
