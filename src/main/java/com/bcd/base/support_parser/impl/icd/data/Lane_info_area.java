package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.SkipMode;

public class Lane_info_area {
    @F_num(len = 1)
    public short lane_id;
    @F_num(len = 2)
    public int car_count;
    @F_num(len = 1)
    public short occupancy;
    @F_num(len = 2)
    public int ave_car_speed;
    @F_num(len = 4)
    public long car_distribute;
    @F_num(len = 2)
    public int head_car_pos;
    @F_num(len = 2)
    public int head_car_speed;
    @F_num(len = 2)
    public int tail_car_pos;
    @F_num(len = 2)
    public int tail_car_speed;

    @F_skip(mode = SkipMode.ReservedFromStart,len = 32)
    public byte[] reserved;

}
