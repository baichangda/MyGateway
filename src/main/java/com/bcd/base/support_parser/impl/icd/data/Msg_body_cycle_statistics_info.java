package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.*;

import java.util.List;

public class Msg_body_cycle_statistics_info implements Msg_body {
    @F_num(type = NumType.uint16)
    public int period;
    @F_num(type = NumType.uint8)
    public short len_A;
    @F_num(type = NumType.uint8)
    public short len_B;
    @F_num(type = NumType.uint8)
    public short len_C;
    @F_num(type = NumType.uint16, var = 'a')
    public int src_count;
    @F_num(type = NumType.uint8, var = 'b')
    public short lane_count;
    @F_skip(len = 32, mode = SkipMode.reservedFromStart)
    public byte reserved;
    @F_num_array(lenExpr = "a", singleType = NumType.uint32)
    public long[] src_array;
    @F_bean_list(listLenExpr = "b")
    public List<Lane_info_cycle> lane_info_array;

}
