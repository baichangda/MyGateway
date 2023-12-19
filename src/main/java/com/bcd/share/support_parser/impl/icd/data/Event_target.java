package com.bcd.share.support_parser.impl.icd.data;


import com.bcd.share.support_parser.anno.*;
import com.bcd.share.support_parser.impl.icd.processor.Target_info_extras_processor;

@C_skip(len = 64)
public class Event_target {
    @F_num(type = NumType.uint32)
    public long track_id;
    @F_num(type = NumType.uint32,  valExpr = "x/10000000")
    public double lon;
    @F_num(type = NumType.uint32,  valExpr = "x/10000000")
    public double lat;
    @F_num(type = NumType.uint32)
    public long alt;
    @F_num(type = NumType.uint8)
    public TargetClass targetClass;
    @F_customize(processorClass = Target_info_extras_processor.class)
    public Target_info_extras extras;

}
