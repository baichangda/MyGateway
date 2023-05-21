package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_bean_list;
import com.bcd.base.support_parser.anno.F_num;

import java.util.List;

public class Road_info {
    @F_num(len = 4)
    public long road_id;
    @F_num(len = 4,var = 'a')
    public long lane_count;
    @F_bean_list(listLenExpr = "a")
    public List<Road_info_lane> lane_info_array;
}
