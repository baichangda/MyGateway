package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.anno.DateMode;
import com.bcd.base.support_parser.anno.F_date;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

import java.util.Date;

public class Position {
    //状态
    @F_num(type = NumType.uint32)
    public long status;
    //维度
    @F_num(type = NumType.uint32, valType = NumType.float64, valExpr = "n/1000000")
    public double lat;
    //经度
    @F_num(type = NumType.uint32, valType = NumType.float64, valExpr = "n/1000000")
    public double lng;
    //高程
    @F_num(type = NumType.uint16)
    public int alt;
    //速度
    @F_num(type = NumType.uint16, valType = NumType.float32, valExpr = "n/10")
    public float speed;
    //方向
    @F_num(type = NumType.uint16)
    public int direction;
    //时间
    @F_date(mode = DateMode.bytes_yyMMddHHmmss, baseYear = 2000, zoneId = "+8")
    public Date time;
}
