package com.bcd.share.support_parser.impl.icd.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SensorStatus {
    invalid(0x00, "无效"),
    self_test(0x01, "自检"),
    normal(0x02, "正常"),
    exception(0x03, "异常"),
    ;
    @JsonValue
    public final int type;
    public final String remark;

    SensorStatus(int type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    @JsonCreator
    public static SensorStatus fromInteger(int type) {
        for (SensorStatus value : SensorStatus.values()) {
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
