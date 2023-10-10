package com.bcd.share.support_parser.impl.icd.data;

import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.F_skip;
import com.bcd.share.support_parser.anno.NumType;
import com.bcd.share.support_parser.anno.SkipMode;

public class Sensor_body_millimeter_wave_radar implements Sensor_body {
    @F_num(type = NumType.int32)
    public int distance;

    @F_skip(mode = SkipMode.reservedFromStart, len = 32)
    public byte reserved;
}
