package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.C_skip;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

@C_skip(len = 16)
public class Lane_info_queue {
    @F_num(type = NumType.uint8)
    public short lane_id;
    @F_num(type = NumType.uint16)
    public int len;
    @F_num(type = NumType.uint16)
    public int head_car_pos;
    @F_num(type = NumType.uint16)
    public int tail_car_pos;
    @F_num(type = NumType.uint16)
    public int car_count;
}
