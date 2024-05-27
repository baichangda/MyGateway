package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_string;
import com.bcd.base.support_parser.anno.NumType;

public class Target_info_extras_car implements Target_info_extras {
    @F_num(type = NumType.uint32)
    public CarType type;
    @F_string(len = 16)
    public String lic_plate;
    @F_num(type = NumType.uint16)
    public CarColor color;
    @F_num(type = NumType.int8)
    public byte status;
}
