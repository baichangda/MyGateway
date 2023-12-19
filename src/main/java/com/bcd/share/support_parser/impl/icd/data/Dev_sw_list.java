package com.bcd.share.support_parser.impl.icd.data;

import com.bcd.share.support_parser.anno.C_skip;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.F_skip;
import com.bcd.share.support_parser.anno.NumType;

@C_skip(len = 32)
public class Dev_sw_list {
    @F_num(type = NumType.int8)
    public byte data_transfer;

}
