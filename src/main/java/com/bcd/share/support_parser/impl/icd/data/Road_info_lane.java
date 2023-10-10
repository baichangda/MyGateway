package com.bcd.share.support_parser.impl.icd.data;

import com.bcd.share.support_parser.anno.F_bean_list;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;

import java.util.List;

public class Road_info_lane {
    @F_num(type = NumType.uint32)
    public long lane_id;
    @F_num(type = NumType.uint16, var = 'a')
    public int target_count;
    @F_bean_list(listLenExpr = "a")
    public List<Road_info_lane_target> lane_target_array;
}
