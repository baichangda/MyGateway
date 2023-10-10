package com.bcd.root.support_parser.impl.icd.data;


import com.bcd.root.support_parser.anno.*;

public class Dev_hw_list {
    @F_string(len = 16)
    public String computing_power;

    @F_num(type = NumType.uint16, valExpr = "x/10")
    public int mem_cap;

    @F_num(type = NumType.uint16)
    public int in_storage_cap;

    @F_num(type = NumType.uint16)
    public int ext_storage_cap;

    @F_num(type = NumType.uint16)
    public int bandwidth;

    @F_skip(mode = SkipMode.reservedFromStart, len = 32)
    public byte reserved;


}
