package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

public class Target_info_extras_barrier implements Target_info_extras {
    @F_num(type = NumType.uint16)
    public int type;
}
