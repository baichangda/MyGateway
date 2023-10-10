package com.bcd.root.support_parser.impl.icd.data;

import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.F_skip;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.anno.SkipMode;

public class Dev_sw_list {
    @F_num(type = NumType.int8)
    public byte data_transfer;

    @F_skip(mode = SkipMode.reservedFromStart, len = 32)
    public byte reserved;
}
