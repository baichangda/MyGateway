package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

/**
 * 发动机数据
 */
public class VehicleEngineData {
    //发动机状态
    @F_num(type = NumType.uint8)
    public short status;

    //曲轴转速
    @F_num(type = NumType.uint16)
    public int speed;

    //燃料消耗率
    @F_num(type = NumType.uint16, valExpr = "x/100")
    public float rate;

}
