package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_num_array;

/**
 * 报警数据
 */
public class VehicleAlarmData {
    //最高报警等级
    @F_num(len = 1)
    public short maxAlarmLevel;

    //最高电压电池单体代号
    @F_num(len = 4)
    public int alarmFlag;

    //可充电储能装置故障总数
    @F_num(len = 1, var = 'a')
    public short chargeBadNum;

    //可充电储能装置故障代码列表
    @F_num_array(lenExpr = "a", singleLen = 4)
    public long[] chargeBadCodes;

    //驱动电机故障总数
    @F_num(len = 1, var = 'b')
    public short driverBadNum;

    //驱动电机故障代码列表
    @F_num_array(lenExpr = "b", singleLen = 4)
    public int[] driverBadCodes;

    //发动机故障总数
    @F_num(len = 1, var = 'c')
    public short engineBadNum;

    //发动机故障代码列表
    @F_num_array(lenExpr = "c", singleLen = 1)
    public short[] engineBadCodes;

    //其他故障总数
    @F_num(len = 1, var = 'd')
    public short otherBadNum;

    //其他故障代码列表
    @F_num_array(lenExpr = "d", singleLen = 4)
    public long[] otherBadCodes;


}
