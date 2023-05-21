package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.F_string;
import com.bcd.base.support_parser.anno.SkipMode;

public class Dev_hw_list {
    @F_string(len = 16)
    public String computing_power;

    @F_num(len = 2,valExpr = "x/10")
    public int mem_cap;

    @F_num(len = 2)
    public int in_storage_cap;

    @F_num(len = 2)
    public int ext_storage_cap;

    @F_num(len = 2)
    public int bandwidth;

    @F_skip(mode = SkipMode.ReservedFromStart,len = 32)
    public byte[] reserved;


}
