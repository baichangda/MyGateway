package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.anno.DateMode;
import com.bcd.base.support_parser.anno.F_date;

import java.util.Date;

public class QueryServerTimeResponse implements PacketBody {
    //服务器时间
    @F_date(mode = DateMode.bytes_yyMMddHHmmss, baseYear = 2000, zoneId = "+0")
    public Date serverTime;
}
