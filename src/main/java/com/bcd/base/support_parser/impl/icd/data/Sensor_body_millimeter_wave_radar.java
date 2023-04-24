package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.SkipMode;

public class Sensor_body_millimeter_wave_radar implements Sensor_body {
    @F_integer(len = 4)
    public int distance;

    @F_skip(mode = SkipMode.ReservedFromStart,len = 32)
    public byte[] reserved;
}
