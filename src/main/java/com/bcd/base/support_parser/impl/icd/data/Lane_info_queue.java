package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.SkipMode;

public class Lane_info_queue {
    @F_integer(len = 1)
    public short lane_id;
    @F_integer(len = 2)
    public int len;
    @F_integer(len = 2)
    public int head_car_pos;
    @F_integer(len = 2)
    public int tail_car_pos;
    @F_integer(len = 2)
    public int car_count;

    @F_skip(mode = SkipMode.ReservedFromStart,len = 16)
    public byte[] reserved;
}
