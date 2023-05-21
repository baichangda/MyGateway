package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.anno.DateMode;
import com.bcd.base.support_parser.anno.F_date;
import com.bcd.base.support_parser.anno.F_num;

import java.util.Date;

public class VehicleLogoutData implements PacketData{
    //登出时间
    @F_date(mode = DateMode.Bytes_yyMMddHHmmss, baseYear = 2000)
    public Date collectTime;

    //登出流水号
    @F_num(len = 2)
    public int sn;
}
