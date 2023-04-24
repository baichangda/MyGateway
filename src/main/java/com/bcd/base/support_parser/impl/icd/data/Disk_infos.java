package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_string;

public class Disk_infos {
    @F_string(len = 32)
    public String disk_id;
    @F_integer(len = 8)
    public long size;
    @F_integer(len = 1)
    public short usage;
}
