package com.bcd.root.support_parser.impl.icd.data;

import com.bcd.root.support_parser.anno.F_bean_list;
import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.NumType;

import java.util.List;

public class Road_info {
    @F_num(type = NumType.uint32)
    public long road_id;
    @F_num(type = NumType.uint32, var = 'a')
    public long lane_count;
    @F_bean_list(listLenExpr = "a")
    public List<Road_info_lane> lane_info_array;
}
