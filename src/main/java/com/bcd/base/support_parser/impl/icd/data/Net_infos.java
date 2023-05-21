package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_num_array;
import com.bcd.base.support_parser.anno.F_string;

public class Net_infos {
    @F_string(len = 16)
    public String net_name;
    @F_num_array(len = 4,singleLen = 1)
    public short[] ipv4_addr;
    @F_num_array(len = 4,singleLen = 1)
    public short[] ipv4_mask;
    @F_num_array(len = 4,singleLen = 1)
    public short[] ipv4_gateway;
    @F_num(len = 8)
    public long send_rate;
    @F_num(len = 8)
    public long recv_rate;
}
