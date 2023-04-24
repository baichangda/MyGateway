package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_bean_list;
import com.bcd.base.support_parser.anno.F_integer;

import java.util.List;

public class Road_info_lane {
    @F_integer(len = 4)
    public long lane_id;
    @F_integer(len = 2,var = 'a')
    public int target_count;
    @F_bean_list(listLenExpr = "a")
    public List<Road_info_lane_target> lane_target_array;
}
