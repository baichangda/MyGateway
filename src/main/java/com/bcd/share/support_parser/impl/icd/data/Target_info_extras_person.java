package com.bcd.share.support_parser.impl.icd.data;

import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;

public class Target_info_extras_person implements Target_info_extras {
    @F_num(type = NumType.uint8)
    public short type;
    @F_num(type = NumType.uint8)
    public short behavior;
}
