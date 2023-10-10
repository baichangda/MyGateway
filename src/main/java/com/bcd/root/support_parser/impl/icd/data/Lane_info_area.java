package com.bcd.root.support_parser.impl.icd.data;

import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.F_skip;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.anno.SkipMode;

public class Lane_info_area {
    @F_num(type = NumType.uint8)
    public short lane_id;
    @F_num(type = NumType.uint16)
    public int car_count;
    @F_num(type = NumType.uint8)
    public short occupancy;
    @F_num(type = NumType.uint16)
    public int ave_car_speed;
    @F_num(type = NumType.uint32)
    public long car_distribute;
    @F_num(type = NumType.uint16)
    public int head_car_pos;
    @F_num(type = NumType.uint16)
    public int head_car_speed;
    @F_num(type = NumType.uint16)
    public int tail_car_pos;
    @F_num(type = NumType.uint16)
    public int tail_car_speed;
    @F_skip(mode = SkipMode.reservedFromStart, len = 32)
    public byte reserved;

}
