package com.bcd.base.support_parser.impl.icd.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SensorType {

    invalid(0x00, "无效"),
    camera(0x01, "相机"),
    millimeter_wave_radar(0x02, "毫米波雷达"),
    lidar(0x03, "激光雷达"),
    ;
    @JsonValue
    public final int type;
    public final String remark;

    SensorType(int type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    @JsonCreator
    public static SensorType fromInteger(int type) {
        for (SensorType value : SensorType.values()) {
            if (value.type == type) {
                return value;
            }
        }
        return null;
    }

    public int toInteger() {
        return type;
    }
}
