package com.bcd.root.support_parser.impl.icd.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CarType {
    sedan(0x01, "sedan"),
    suv(0x02, "suv"),
    van(0x03, "van"),
    hatchback(0x04, "hatchback"),
    mpv(0x05, "mpv"),
    pickup(0x06, "pickup"),
    bus(0x07, "bus"),
    truck(0x08, "truck"),
    estate(0x09, "estate"),
    ;
    @JsonValue
    public final int type;
    public final String remark;

    CarType(int type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    @JsonCreator
    public static CarType fromInteger(int type) {
        for (CarType value : CarType.values()) {
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
