package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.SkipMode;

public class Dev_func_list {
    @F_integer(len = 1)
    public byte target_detection;
    @F_integer(len = 1)
    public byte lane_detection;
    @F_integer(len = 1)
    public byte event_detection;
    @F_integer(len = 1)
    public byte period_detection;
    @F_integer(len = 1)
    public byte area_detection;
    @F_integer(len = 1)
    public byte trigger_detection;
    @F_integer(len = 1)
    public byte queue_detection;
    @F_integer(len = 1)
    public byte status_monitor;
    @F_integer(len = 1)
    public byte environment_monitor;

    @F_skip(mode = SkipMode.ReservedFromStart,len = 32)
    public byte[] reserved;
}
