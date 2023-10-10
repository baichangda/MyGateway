package com.bcd.share.support_parser.impl.icd.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventType {
    congestion_reminder(0x0001, "拥堵提示"),
    abnormal_parking(0x0002, "异常停车"),
    solid_lane_change(0x0003, "实线变道"),
    line_driving(0x0004, "压线行驶"),
    speeding(0x0005, "超速行驶"),
    retrograde_recognition(0x0006, "逆行识别"),
    not_vehicle_retrograde_recognition(0x0007, "非机动车逆行识别"),
    road_debris(0x0008, "道路遗撒"),
    pedestrians_break_into(0x0009, "行人闯入"),
    lane_anomaly_detection(0x0010, "车道异常检测"),
    zebra_crossing_not_slow_down(0x0011, "斑马线未减速"),
    unlicensed_vehicle_identification(0x0012, "无牌机动车辆识别"),
    intersection_collision_warning(0x0013, "交叉路口碰撞预警"),
    vehicle_double_flash_anomaly_detection(0x0014, "车辆双闪异常检测"),
    not_keeping_safe_distance(0x0015, "未保持安全距离"),
    bicycle_carrying_people(0x0015, "(电动)自行车载人"),
    ;
    @JsonValue
    public final int type;
    public final String remark;

    EventType(int type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    @JsonCreator
    public static EventType fromInteger(int type) {
        for (EventType value : EventType.values()) {
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
