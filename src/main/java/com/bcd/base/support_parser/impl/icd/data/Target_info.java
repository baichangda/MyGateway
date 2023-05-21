package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.impl.icd.processor.Target_info_extras_processor;

public class Target_info {
    @F_num(len = 2)
    public int target_num;
    @F_num(len = 4)
    public long track_id;
    @F_num(len = 4)
    public long lane_id;
    @F_num(len = 4, valExpr = "x/10000000")
    public double lon;
    @F_num(len = 4, valExpr = "x/10000000")
    public double lat;
    @F_num(len = 4)
    public long alt;
    @F_num(len = 4)
    public int dev_x;
    @F_num(len = 4)
    public int dev_y;
    @F_num(len = 4)
    public int dev_z;
    @F_num(len = 2, valExpr = "x/100")
    public float azimuth_angle;
    @F_num(len = 4)
    public int dev_vx;
    @F_num(len = 4)
    public int dev_vy;
    @F_num(len = 4)
    public int dev_vz;

    @F_num(len = 2)
    public int len;
    @F_num(len = 2)
    public int width;
    @F_num(len = 2)
    public int height;
    @F_num(len = 1)
    public TargetClass clazz;
    @F_num(len = 1)
    public short class_cfd;
    @F_num(len = 2)
    public int img_x;
    @F_num(len = 2)
    public int img_y;
    @F_num(len = 2)
    public int img_len;
    @F_num(len = 2)
    public int img_width;
    @F_num(len = 2)
    public int img_height;

    @F_num(len = 2, unsigned = false, valExpr = "x/100")
    public float img_direc_len;
    @F_num(len = 2, unsigned = false, valExpr = "x/100")
    public float img_direc_width;
    @F_num(len = 2, unsigned = false, valExpr = "x/100")
    public float img_direc_height;

    @F_num(len = 1)
    public byte if_extras;

    @F_customize(processorClass = Target_info_extras_processor.class)
    public Target_info_extras extras;

    @F_skip(mode = SkipMode.ReservedFromStart, len = 128)
    public byte[] reserved;

}
