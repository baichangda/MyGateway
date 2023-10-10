package com.bcd.share.support_parser.impl.icd.data;

import com.bcd.share.support_parser.anno.F_bean_list;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;

import java.util.List;

public class Msg_body_event_info implements Msg_body {
    @F_num(type = NumType.uint16, var = 'a')
    public int event_count;
    @F_bean_list(listLenExpr = "a")
    public List<Event_info> event_info_array;

}
