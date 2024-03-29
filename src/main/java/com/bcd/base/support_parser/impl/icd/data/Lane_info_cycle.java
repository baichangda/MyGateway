package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.C_skip;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

@C_skip(len = 32)
public class Lane_info_cycle {
    @F_num(type = NumType.uint8)
    public short lane_id;
    @F_num(type = NumType.uint16)
    public int total_car;
    @F_num(type = NumType.uint16)
    public int car_a_count;
    @F_num(type = NumType.uint16)
    public int car_b_count;
    @F_num(type = NumType.uint16)
    public int car_c_count;
    @F_num(type = NumType.uint8)
    public short occupancy;
    @F_num(type = NumType.uint16)
    public int ave_speed;
    @F_num(type = NumType.uint8)
    public short ave_car_len;
    @F_num(type = NumType.uint8)
    public short ave_car_head_dis;
    @F_num(type = NumType.uint8)
    public short ave_car_body_dis;
}
