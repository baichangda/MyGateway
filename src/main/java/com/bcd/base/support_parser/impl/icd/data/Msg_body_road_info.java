package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_bean_list;
import com.bcd.base.support_parser.anno.F_num;

import java.util.List;

public class Msg_body_road_info implements Msg_body{
    @F_num(len = 2,var = 'a')
    public int road_count;
    @F_bean_list(listLenExpr = "a")
    public List<Road2_info> road_info_array;

}
