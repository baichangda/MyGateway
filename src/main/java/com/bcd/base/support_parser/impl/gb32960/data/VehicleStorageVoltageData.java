package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.anno.F_bean_list;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

/**
 * 可充电储能装置电压数据
 */
public class VehicleStorageVoltageData {
    //可充电储能子系统个数
    @F_num(type = NumType.uint8, var = 'a')
    public short num;

    //可充电储能子系统电压信息集合
    @F_bean_list(listLenExpr = "a * 1 + (0)")
    public StorageVoltageData[] content;
}
