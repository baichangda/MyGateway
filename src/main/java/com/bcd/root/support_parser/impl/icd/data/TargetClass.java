package com.bcd.root.support_parser.impl.icd.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TargetClass {
    person(0x01, "person"),
    bicycle(0x02, "bicycle"),
    car(0x03, "car"),
    motorbike(0x04, "motorbike"),
    bus(0x05, "bus"),
    truck(0x06, "truck"),
    traffic_light(0x07, "traffic_light"),
    fire_hydrant(0x08, "fire_hydrant"),
    stop_sign(0x09, "stop_sign"),
    parking_meter(0x0A, "parking_meter"),
    bench(0x0B, "bench"),
    animal(0x0C, "animal"),
    radar_point(0x0D, "radar_point"),
    radar_car_s(0x0E, "radar_car_s"),
    radar_car_m(0x0F, "radar_car_m"),
    radar_car_l(0x10, "radar_car_l"),
    radar_motor(0x11, "radar_motor"),
    radar_bicycle(0x12, "radar_bicycle"),
    radar_wid(0x13, "radar_wid"),
    radar_person(0x14, "radar_person"),
    ;
    @JsonValue
    public final int type;
    public final String remark;

    TargetClass(int type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    @JsonCreator
    public static TargetClass fromInteger(int type) {
        for (TargetClass value : TargetClass.values()) {
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
