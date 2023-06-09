package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_string;
import com.bcd.base.support_parser.anno.NumType;

public class Disk_infos {
    @F_string(len = 32)
    public String disk_id;
    @F_num(type = NumType.int64)
    public long size;
    @F_num(type = NumType.uint8)
    public short usage;
}
