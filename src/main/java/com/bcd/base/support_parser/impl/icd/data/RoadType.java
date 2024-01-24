package com.bcd.base.support_parser.impl.icd.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoadType {
    invalid(0x00, "无效"),
    intersection(0x01, "路口"),
    road_section(0x02, "路段"),
    ;
    @JsonValue
    public final int type;
    public final String remark;

    RoadType(int type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    @JsonCreator
    public static RoadType fromInteger(int type) {
        for (RoadType value : RoadType.values()) {
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
