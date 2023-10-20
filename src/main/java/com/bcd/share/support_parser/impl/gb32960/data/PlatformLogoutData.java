package com.bcd.share.support_parser.impl.gb32960.data;

import com.bcd.share.support_parser.anno.F_date_bytes_6;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;

import java.util.Date;

public class PlatformLogoutData implements PacketData {
    //登出时间
    @F_date_bytes_6
    public Date collectTime;

    //登出流水号
    @F_num(type = NumType.uint16)
    public int sn;
}
