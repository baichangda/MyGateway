package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_integer;

public class Target_info_extras_barrier implements Target_info_extras{
    @F_integer(len = 2)
    public int type;
}
