package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.anno.F_num;

/**
 * 车辆位置数据
 */
public class VehiclePositionData {
    //定位状态
    @F_num(len = 1)
    public byte status;

    //经度
    @F_num(len = 4, valExpr = "x/1000000")
    public double lng;

    //纬度
    @F_num(len = 4, valExpr = "x/1000000")
    public double lat;
}
