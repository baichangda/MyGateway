package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_string;

public class Disk_infos {
    @F_string(len = 32)
    public String disk_id;
    @F_num(len = 8)
    public long size;
    @F_num(len = 1)
    public short usage;
}
