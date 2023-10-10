package com.bcd.root.support_parser.impl.icd.data;


import com.bcd.root.support_parser.anno.*;

public class Msg_body_device_status_info implements Msg_body {
    @F_num(type = NumType.uint32)
    public long dev_sn;
    @F_num(type = NumType.uint8)
    public SensorStatus dev_status;
    @F_bean
    public Dev_hw_list dev_hw_list;
    @F_bean
    public Dev_sw_list dev_sw_list;
    @F_bean
    public Dev_func_list dev_func_list;

    @F_skip(mode = SkipMode.reservedFromStart, len = 128)
    public byte reserved;
}
