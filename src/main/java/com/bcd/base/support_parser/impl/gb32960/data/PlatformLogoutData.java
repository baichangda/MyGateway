package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.anno.C_impl;
import com.bcd.base.support_parser.anno.F_date_bytes_6;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

import java.util.Date;
@C_impl(value = 0x06)
public class PlatformLogoutData implements PacketData {
    //登出时间
    @F_date_bytes_6
    public Date collectTime;

    //登出流水号
    @F_num(type = NumType.uint16)
    public int sn;
}
