package com.bcd.share.support_parser.impl.icd.data;

import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.F_num_array;
import com.bcd.share.support_parser.anno.F_string;
import com.bcd.share.support_parser.anno.NumType;

public class Net_infos {
    @F_string(len = 16)
    public String net_name;
    @F_num_array(len = 4, singleType = NumType.uint8)
    public short[] ipv4_addr;
    @F_num_array(len = 4, singleType = NumType.uint8)
    public short[] ipv4_mask;
    @F_num_array(len = 4, singleType = NumType.uint8)
    public short[] ipv4_gateway;
    @F_num(type = NumType.int64)
    public long send_rate;
    @F_num(type = NumType.int64)
    public long recv_rate;
}
