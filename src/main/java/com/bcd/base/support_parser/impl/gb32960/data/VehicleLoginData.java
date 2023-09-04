package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.anno.*;

import java.util.Date;

public class VehicleLoginData implements PacketData {
    //数据采集时间
    @F_date(mode = DateMode.bytes_yyMMddHHmmss)
    public Date collectTime;

    //登入流水号
    @F_num(type = NumType.uint16)
    public int sn;

    //iccid
    @F_string(len = 20)
    public String iccid;

    //可充电储能子系统数
    @F_num(type = NumType.uint8, var = 'n')
    public short subSystemNum;

    //可充电储能系统编码长度
    @F_num(type = NumType.uint8, var = 'm')
    public short systemCodeLen;

    //可充电储能系统编码
    @F_string(lenExpr = "n*m")
    public String systemCode;
}
