package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.anno.F_integer;

/**
 * 发动机数据
 */
public class VehicleEngineData {
    //发动机状态
    @F_integer(len = 1)
    public short status;

    //曲轴转速
    @F_integer(len = 2)
    public int speed;

    //燃料消耗率
    @F_integer(len = 2, valExpr = "x/100")
    public float rate;

}
