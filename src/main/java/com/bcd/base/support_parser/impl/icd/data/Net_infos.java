package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_integer_array;
import com.bcd.base.support_parser.anno.F_string;

public class Net_infos {
    @F_string(len = 16)
    public String net_name;
    @F_integer_array(len = 4,singleLen = 1)
    public short[] ipv4_addr;
    @F_integer_array(len = 4,singleLen = 1)
    public short[] ipv4_mask;
    @F_integer_array(len = 4,singleLen = 1)
    public short[] ipv4_gateway;
    @F_integer(len = 8)
    public long send_rate;
    @F_integer(len = 8)
    public long recv_rate;
}
