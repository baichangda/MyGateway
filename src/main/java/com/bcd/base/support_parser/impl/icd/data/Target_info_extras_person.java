package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_num;

public class Target_info_extras_person implements Target_info_extras{
    @F_num(len = 1)
    public short type;
    @F_num(len = 1)
    public short behavior;
}
