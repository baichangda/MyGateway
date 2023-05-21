package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_num_array;

/**
 * 每个可充电储能子系统上温度数据格式
 */
public class StorageTemperatureData {
    //可充电储能子系统号
    @F_num(len = 1)
    public short no;

    //可充电储能温度探针个数
    @F_num(len = 2, var = 'n')
    public int num;

    //可充电储能子系统各温度探针检测到的温度值
    @F_num_array(lenExpr = "n", valExpr = "x-40", singleLen = 1)
    public short[] temperatures;
}
