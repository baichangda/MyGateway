package com.bcd.base.support_parser.impl.icd.processor;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.icd.data.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;

public class Sensor_body_processor implements Processor<Sensor_body> {
    @Override
    public Sensor_body process(ByteBuf data, ProcessContext parentContext) {
        final Sensor_info instance = (Sensor_info) parentContext.instance;
        final Sensor_body sensor_body;
        switch (instance.sensor_type){
            case camera -> {
                sensor_body = Parser.parse(Sensor_body_camera.class, data, parentContext);
            }
            case millimeter_wave_radar -> {
                sensor_body = Parser.parse(Sensor_body_millimeter_wave_radar.class, data, parentContext);
            }
            case lidar -> {
                sensor_body = Parser.parse(Sensor_body_lidar.class, data, parentContext);
            }
            default -> {
                throw BaseRuntimeException.getException("sensor_type[{}] not support",instance.sensor_type);
            }
        }
        return sensor_body;
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext parentContext, Sensor_body instance) {
        Parser.deParse(instance, data, parentContext);
    }
}
