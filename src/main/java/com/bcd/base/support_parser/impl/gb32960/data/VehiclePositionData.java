package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

/**
 * 车辆位置数据
 */
public class VehiclePositionData {
    //定位状态
    @F_num(type = NumType.int8)
    public byte status;

    //经度
    @F_num(type = NumType.uint32, valType = NumType.float64, valExpr = "x/1000000")
    public double lng;

    //纬度
    @F_num(type = NumType.uint32, valType = NumType.float64, valExpr = "x/1000000")
    public double lat;
}
