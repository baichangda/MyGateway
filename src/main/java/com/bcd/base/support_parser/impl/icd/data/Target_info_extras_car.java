package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_string;

public class Target_info_extras_car implements Target_info_extras{
    @F_integer(len = 4)
    public CarType type;
    @F_string(len = 16)
    public short lic_plate;
    @F_integer(len = 2)
    public CarColor color;
    @F_integer(len = 1)
    public byte status;
}
