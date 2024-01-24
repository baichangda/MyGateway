package com.bcd.base.support_parser.impl.icd.data;


import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.impl.icd.processor.Sensor_body_processor;

@C_skip(len = 128)
public class Sensor_info {
    @F_num(type = NumType.uint32)
    public long sensor_id;
    @F_num(type = NumType.uint32)
    public long sensor_sn;
    @F_num(type = NumType.uint8)
    public SensorType sensor_type;
    @F_num(type = NumType.uint8)
    public DataType data_type;
    @F_num(type = NumType.uint32,  valExpr = "x/10000000")
    public double sensor_lon;
    @F_num(type = NumType.uint32,  valExpr = "x/10000000")
    public double sensor_lat;
    @F_num(type = NumType.uint32)
    public long sensor_alt;
    @F_num(type = NumType.uint16,  valExpr = "x/100")
    public float sensor_azimuth;
    @F_num(type = NumType.uint16,  valExpr = "x/100")
    public float sensor_pitch;
    @F_num(type = NumType.uint16,  valExpr = "x/100")
    public float sensor_roll;
    @F_num(type = NumType.uint8)
    public SensorStatus sensor_status;
    @F_customize(processorClass = Sensor_body_processor.class)
    public Sensor_body sensor_body;

}
