package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.*;

import java.util.List;

public class Msg_body_area_statistics_info implements Msg_body{
    @F_integer(len = 2,valExpr = "x/10")
    public float period;
    @F_integer(len = 2)
    public int area_dis_near;
    @F_integer(len = 2)
    public int area_dis_far;
    @F_integer(len = 2,var = 'a')
    public int src_count;
    @F_integer(len = 1,var = 'b')
    public short lane_count;

    @F_skip(len = 32,mode = SkipMode.ReservedFromStart)
    public byte[] reserved;
    @F_integer_array(lenExpr = "a", singleLen = 4)
    public long[] src_array;
    @F_bean_list(listLenExpr = "b")
    public List<Lane_info_area> lane_info_array;

}
