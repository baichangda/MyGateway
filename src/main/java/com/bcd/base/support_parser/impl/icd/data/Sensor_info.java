package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.impl.icd.processor.Sensor_body_processor;

public class Sensor_info {
    @F_num(len = 4)
    public long sensor_id;
    @F_num(len = 4)
    public long sensor_sn;
    @F_num(len = 1)
    public SensorType sensor_type;
    @F_num(len = 1)
    public DataType data_type;
    @F_num(len = 4, valExpr = "x/10000000")
    public double sensor_lon;
    @F_num(len = 4, valExpr = "x/10000000")
    public double sensor_lat;
    @F_num(len = 4)
    public long sensor_alt;
    @F_num(len = 2, valExpr = "x/100")
    public float sensor_azimuth;
    @F_num(len = 2, valExpr = "x/100")
    public float sensor_pitch;
    @F_num(len = 2, valExpr = "x/100")
    public float sensor_roll;
    @F_num(len = 1)
    public SensorStatus sensor_status;
    @F_customize(processorClass = Sensor_body_processor.class)
    public Sensor_body sensor_body;

    @F_skip(mode = SkipMode.ReservedFromStart,len = 128)
    public byte[] reserved;
}
