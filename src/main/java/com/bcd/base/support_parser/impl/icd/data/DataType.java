package com.bcd.base.support_parser.impl.icd.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DataType {

    invalid(0x00,"无效"),
    visual_image(0x01,"视觉图像"),
    millimeter_wave_radar_structured(0x02,"毫米波雷达结构化"),
    millimeter_wave_radar_point_cloud(0x03,"毫米波雷达点云"),
    lidar_structured(0x04,"激光雷达结构化"),
    fusion_data(0x05,"融合数据"),
    lidar_point_cloud(0x06,"激光雷达点云"),
    ;

    @JsonValue
    public final int type;
    public final String remark;

    DataType(int type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    @JsonCreator
    public static DataType fromInteger(int type){
        for (DataType value : DataType.values()) {
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
