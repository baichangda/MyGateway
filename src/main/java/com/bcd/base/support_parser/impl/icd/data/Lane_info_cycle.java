package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.SkipMode;

public class Lane_info_cycle {
    @F_num(len = 1)
    public short lane_id;
    @F_num(len = 2)
    public int total_car;
    @F_num(len = 2)
    public int car_a_count;
    @F_num(len = 2)
    public int car_b_count;
    @F_num(len = 2)
    public int car_c_count;
    @F_num(len = 1)
    public short occupancy;
    @F_num(len = 2)
    public int ave_speed;
    @F_num(len = 1)
    public short ave_car_len;
    @F_num(len = 1)
    public short ave_car_head_dis;
    @F_num(len = 1)
    public short ave_car_body_dis;

    @F_skip(mode = SkipMode.ReservedFromStart,len = 32)
    public byte[] reserved;

}
