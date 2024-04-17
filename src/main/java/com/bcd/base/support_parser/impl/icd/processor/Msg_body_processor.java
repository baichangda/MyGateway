package com.bcd.base.support_parser.impl.icd.processor;

import com.bcd.base.exception.BaseRuntimeException;
import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.impl.icd.data.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;


public class Msg_body_processor implements Processor<Msg_body> {

    final Processor<Msg_body_system_runtime_info> processor_msg_body_system_runtime_info = Parser.getProcessor(Msg_body_system_runtime_info.class);
    final Processor<Msg_body_sensor_status_info> processor_msg_body_sensor_status_info = Parser.getProcessor(Msg_body_sensor_status_info.class);
    final Processor<Msg_body_device_status_info> processor_msg_body_device_status_info = Parser.getProcessor(Msg_body_device_status_info.class);
    final Processor<Msg_body_target_detect_info> processor_msg_body_target_detect_info = Parser.getProcessor(Msg_body_target_detect_info.class);
    final Processor<Msg_body_lane_detect_info> processor_msg_body_lane_detect_info = Parser.getProcessor(Msg_body_lane_detect_info.class);
    final Processor<Msg_body_cycle_statistics_info> processor_msg_body_cycle_statistics_info = Parser.getProcessor(Msg_body_cycle_statistics_info.class);
    final Processor<Msg_body_area_statistics_info> processor_msg_body_area_statistics_info = Parser.getProcessor(Msg_body_area_statistics_info.class);
    final Processor<Msg_body_trigger_statistics_info> processor_msg_body_trigger_statistics_info = Parser.getProcessor(Msg_body_trigger_statistics_info.class);
    final Processor<Msg_body_queue_statistics_info> processor_msg_body_queue_statistics_info = Parser.getProcessor(Msg_body_queue_statistics_info.class);
    final Processor<Msg_body_event_info> processor_msg_body_event_info = Parser.getProcessor(Msg_body_event_info.class);
    final Processor<Msg_body_road_info> processor_msg_body_road_info = Parser.getProcessor(Msg_body_road_info.class);

    public static void main(String[] args) {
        System.out.println("[" + new String(new byte[1]) + "]");
    }

    @Override
    public Msg_body process(ByteBuf data, ProcessContext parentContext) {
        final Msg msg = (Msg) parentContext.instance;
        final Msg_body msg_body;
        switch (msg.msg_header.frame_type) {
            case system_runtime_info -> {
                msg_body = processor_msg_body_system_runtime_info.process(data, parentContext);
            }
            case sensor_status_info -> {
                msg_body = processor_msg_body_sensor_status_info.process(data, parentContext);
            }
            case device_status_info -> {
                msg_body = processor_msg_body_device_status_info.process(data, parentContext);
            }
            case target_detect_info -> {
                msg_body = processor_msg_body_target_detect_info.process(data, parentContext);
            }
            case lane_detect_info -> {
                msg_body = processor_msg_body_lane_detect_info.process(data, parentContext);
            }
            case cycle_statistics_info -> {
                msg_body = processor_msg_body_cycle_statistics_info.process(data, parentContext);
            }
            case area_statistics_info -> {
                msg_body = processor_msg_body_area_statistics_info.process(data, parentContext);
            }
            case trigger_statistics_info -> {
                msg_body = processor_msg_body_trigger_statistics_info.process(data, parentContext);
            }
            case queue_statistics_info -> {
                msg_body = processor_msg_body_queue_statistics_info.process(data, parentContext);
            }
            case event_info -> {
                msg_body = processor_msg_body_event_info.process(data, parentContext);
            }
            case road_info -> {
                msg_body = processor_msg_body_road_info.process(data, parentContext);
            }
            default -> {
                throw BaseRuntimeException.get("frame_type[{}] not support", msg.msg_header.frame_type);
            }
        }
        return msg_body;
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext parentContext, Msg_body instance) {
        final Msg msg = (Msg) parentContext.instance;
        switch (msg.msg_header.frame_type) {
            case system_runtime_info -> {
                processor_msg_body_system_runtime_info.deProcess(data, parentContext, (Msg_body_system_runtime_info) instance);
            }
            case sensor_status_info -> {
                processor_msg_body_sensor_status_info.deProcess(data, parentContext, (Msg_body_sensor_status_info) instance);
            }
            case device_status_info -> {
                processor_msg_body_device_status_info.deProcess(data, parentContext, (Msg_body_device_status_info) instance);
            }
            case target_detect_info -> {
                processor_msg_body_target_detect_info.deProcess(data, parentContext, (Msg_body_target_detect_info) instance);
            }
            case lane_detect_info -> {
                processor_msg_body_lane_detect_info.deProcess(data, parentContext, (Msg_body_lane_detect_info) instance);
            }
            case cycle_statistics_info -> {
                processor_msg_body_cycle_statistics_info.deProcess(data, parentContext, (Msg_body_cycle_statistics_info) instance);
            }
            case area_statistics_info -> {
                processor_msg_body_area_statistics_info.deProcess(data, parentContext, (Msg_body_area_statistics_info) instance);
            }
            case trigger_statistics_info -> {
                processor_msg_body_trigger_statistics_info.deProcess(data, parentContext, (Msg_body_trigger_statistics_info) instance);
            }
            case queue_statistics_info -> {
                processor_msg_body_queue_statistics_info.deProcess(data, parentContext, (Msg_body_queue_statistics_info) instance);
            }
            case event_info -> {
                processor_msg_body_event_info.deProcess(data, parentContext, (Msg_body_event_info) instance);
            }
            case road_info -> {
                processor_msg_body_road_info.deProcess(data, parentContext, (Msg_body_road_info) instance);
            }
            default -> {
                throw BaseRuntimeException.get("frame_type[{}] not support", msg.msg_header.frame_type);
            }
        }
    }
}
