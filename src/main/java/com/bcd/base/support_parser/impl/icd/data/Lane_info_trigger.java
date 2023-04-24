package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.SkipMode;

public class Lane_info_trigger {
    @F_integer(len = 4)
    public long track_id;
    @F_integer(len = 1)
    public short lane_id;
    @F_integer(len = 2)
    public int lane_dis;
    @F_integer(len = 2)
    public int speed;
    @F_integer(len = 1)
    public short status;

    @F_skip(mode = SkipMode.ReservedFromStart,len = 16)
    public byte[] reserved;
}
