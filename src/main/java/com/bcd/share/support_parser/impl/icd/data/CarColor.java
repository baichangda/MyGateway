package com.bcd.share.support_parser.impl.icd.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CarColor {

    yellow(0x0001, "yellow"),
    orange(0x0002, "orange"),
    green(0x0003, "green"),
    gray(0x0004, "gray"),
    red(0x0005, "red"),
    blue(0x0006, "blue"),
    white(0x0007, "white"),
    golden(0x0008, "golden"),
    brown(0x0009, "brown"),
    black(0x000A, "black"),
    ;
    @JsonValue
    public final int type;
    public final String remark;

    CarColor(int type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    @JsonCreator
    public static CarColor fromInteger(int type) {
        for (CarColor value : CarColor.values()) {
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
