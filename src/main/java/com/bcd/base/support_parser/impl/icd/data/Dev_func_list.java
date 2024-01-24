package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.C_skip;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

@C_skip(len = 32)
public class Dev_func_list {
    @F_num(type = NumType.uint8)
    public byte target_detection;
    @F_num(type = NumType.uint8)
    public byte lane_detection;
    @F_num(type = NumType.uint8)
    public byte event_detection;
    @F_num(type = NumType.uint8)
    public byte period_detection;
    @F_num(type = NumType.uint8)
    public byte area_detection;
    @F_num(type = NumType.uint8)
    public byte trigger_detection;
    @F_num(type = NumType.uint8)
    public byte queue_detection;
    @F_num(type = NumType.uint8)
    public byte status_monitor;
    @F_num(type = NumType.uint8)
    public byte environment_monitor;
}
