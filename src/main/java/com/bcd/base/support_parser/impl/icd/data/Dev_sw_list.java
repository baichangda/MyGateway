package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.SkipMode;

public class Dev_sw_list {
    @F_integer(len = 1)
    public byte data_transfer;

    @F_skip(mode = SkipMode.ReservedFromStart,len = 32)
    public byte[] reserved;
}
