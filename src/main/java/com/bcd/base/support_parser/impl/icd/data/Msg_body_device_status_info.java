package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_bean;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.SkipMode;

public class Msg_body_device_status_info implements Msg_body {
    @F_num(len = 4)
    public long dev_sn;
    @F_num(len = 1)
    public SensorStatus dev_status;
    @F_bean
    public Dev_hw_list dev_hw_list;
    @F_bean
    public Dev_sw_list dev_sw_list;
    @F_bean
    public Dev_func_list dev_func_list;

    @F_skip(mode = SkipMode.ReservedFromStart,len = 128)
    public byte[] reserved;
}
