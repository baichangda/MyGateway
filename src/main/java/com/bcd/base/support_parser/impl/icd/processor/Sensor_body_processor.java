package com.bcd.base.support_parser.impl.icd.processor;

import com.bcd.base.exception.MyException;
import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.impl.icd.data.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;

public class Sensor_body_processor implements Processor<Sensor_body> {

    final Processor<Sensor_body_camera> processor_sensor_body_camera = Parser.getProcessor(Sensor_body_camera.class);
    final Processor<Sensor_body_millimeter_wave_radar> processor_sensor_body_millimeter_wave_radar = Parser.getProcessor(Sensor_body_millimeter_wave_radar.class);
    final Processor<Sensor_body_lidar> processor_sensor_body_lidar = Parser.getProcessor(Sensor_body_lidar.class);

    @Override
    public Sensor_body process(ByteBuf data, ProcessContext<?> parentContext) {
        final Sensor_info instance = (Sensor_info) parentContext.instance;
        final Sensor_body sensor_body;
        switch (instance.sensor_type) {
            case camera -> {
                sensor_body = processor_sensor_body_camera.process(data, parentContext);
            }
            case millimeter_wave_radar -> {
                sensor_body = processor_sensor_body_millimeter_wave_radar.process(data, parentContext);
            }
            case lidar -> {
                sensor_body = processor_sensor_body_lidar.process(data, parentContext);
            }
            default -> {
                throw MyException.get("sensor_type[{}] not support", instance.sensor_type);
            }
        }
        return sensor_body;
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext<?> parentContext, Sensor_body instance) {
        final Sensor_info parentInstance = (Sensor_info) parentContext.instance;
        switch (parentInstance.sensor_type) {
            case camera -> {
                processor_sensor_body_camera.deProcess(data, parentContext, (Sensor_body_camera) instance);
            }
            case millimeter_wave_radar -> {
                processor_sensor_body_millimeter_wave_radar.deProcess(data, parentContext, (Sensor_body_millimeter_wave_radar) instance);
            }
            case lidar -> {
                processor_sensor_body_lidar.deProcess(data, parentContext, (Sensor_body_lidar) instance);
            }
            default -> {
                throw MyException.get("sensor_type[{}] not support", parentInstance.sensor_type);
            }
        }
    }
}
