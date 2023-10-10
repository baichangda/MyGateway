package com.bcd.root.support_parser.impl.gb32960.data;

import com.bcd.root.support_parser.anno.F_bean_list;
import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.NumType;


/**
 * 驱动电机数据
 */
public class VehicleMotorData {
    //驱动电机个数
    @F_num(type = NumType.uint8, var = 'a')
    public short num;

    //驱动电机总成信息列表
    @F_bean_list(listLenExpr = "a")
    public MotorData[] content;
}
