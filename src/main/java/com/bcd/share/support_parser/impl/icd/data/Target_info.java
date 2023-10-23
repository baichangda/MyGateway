package com.bcd.share.support_parser.impl.icd.data;


import com.bcd.share.support_parser.anno.*;
import com.bcd.share.support_parser.impl.icd.processor.Target_info_extras_processor;

public class Target_info {
    @F_num(type = NumType.uint16)
    public int target_num;
    @F_num(type = NumType.uint32)
    public long track_id;
    @F_num(type = NumType.uint32)
    public long lane_id;
    @F_num(type = NumType.uint32,  valExpr = "x/10000000")
    public double lon;
    @F_num(type = NumType.uint32,  valExpr = "x/10000000")
    public double lat;
    @F_num(type = NumType.uint32)
    public long alt;
    @F_num(type = NumType.int32)
    public int dev_x;
    @F_num(type = NumType.int32)
    public int dev_y;
    @F_num(type = NumType.int32)
    public int dev_z;
    @F_num(type = NumType.uint16,  valExpr = "x/100")
    public float azimuth_angle;
    @F_num(type = NumType.int32)
    public int dev_vx;
    @F_num(type = NumType.int32)
    public int dev_vy;
    @F_num(type = NumType.int32)
    public int dev_vz;

    @F_num(type = NumType.uint16)
    public int len;
    @F_num(type = NumType.uint16)
    public int width;
    @F_num(type = NumType.uint16)
    public int height;
    @F_num(type = NumType.uint8)
    public TargetClass clazz;
    @F_num(type = NumType.uint8)
    public short class_cfd;
    @F_num(type = NumType.uint16)
    public int img_x;
    @F_num(type = NumType.uint16)
    public int img_y;
    @F_num(type = NumType.uint16)
    public int img_len;
    @F_num(type = NumType.uint16)
    public int img_width;
    @F_num(type = NumType.uint16)
    public int img_height;

    @F_num(type = NumType.int16, valExpr = "x/100")
    public float img_direc_len;
    @F_num(type = NumType.int16, valExpr = "x/100")
    public float img_direc_width;
    @F_num(type = NumType.int16, valExpr = "x/100")
    public float img_direc_height;

    @F_num(type = NumType.int8)
    public byte if_extras;

    @F_customize(processorClass = Target_info_extras_processor.class)
    public Target_info_extras extras;

    @F_skip(mode = SkipMode.reservedFromStart, len = 128)
    public byte reserved;

}
