package com.bcd.base.support_parser.impl.icd.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LaneCanalization {
    invalid(0x00,"无效"),
    straight(0x01,"直行"),
    turn_left(0x02,"左转"),
    turn_right(0x02,"右转"),
    turn_around(0x02,"掉头"),
    straight_and_turn_right(0x02,"直行和右转"),
    straight_and_turn_left(0x02,"直行和左转"),
    straight_and_turn_around(0x02,"直行和掉头"),
    turn_left_and_turn_around(0x02,"左转和掉头"),
    turn_left_or_turn_right(0x02,"仅可左右转弯"),
    ;
    @JsonValue
    public final int type;
    public final String remark;
    LaneCanalization(int type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    @JsonCreator
    public static LaneCanalization fromInteger(int type){
        for (LaneCanalization value : LaneCanalization.values()) {
            if(value.type==type){
                return value;
            }
        }
        return null;
    }

    public int toInteger(){
        return type;
    }
}
