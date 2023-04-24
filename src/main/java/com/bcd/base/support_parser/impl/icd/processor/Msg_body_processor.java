package com.bcd.base.support_parser.impl.icd.processor;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.icd.data.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;


public class Msg_body_processor implements Processor<Msg_body> {
    @Override
    public Msg_body process(ByteBuf data, ProcessContext parentContext) {
        final Msg msg = (Msg) parentContext.instance;
        final Msg_body msg_body;
        switch (msg.msg_header.frame_type) {
            case system_runtime_info -> {
                msg_body = Parser.parse(Msg_body_system_runtime_info.class, data, parentContext);
            }
            case sensor_status_info -> {
                msg_body = Parser.parse(Msg_body_sensor_status_info.class, data, parentContext);
            }
            case device_status_info -> {
                msg_body = Parser.parse(Msg_body_device_status_info.class, data, parentContext);
            }
            case target_detect_info -> {
                msg_body = Parser.parse(Msg_body_target_detect_info.class, data, parentContext);
            }
            case lane_detect_info -> {
                msg_body = Parser.parse(Msg_body_lane_detect_info.class, data, parentContext);
            }
            case cycle_statistics_info -> {
                msg_body = Parser.parse(Msg_body_cycle_statistics_info.class, data, parentContext);
            }
            case area_statistics_info -> {
                msg_body = Parser.parse(Msg_body_area_statistics_info.class, data, parentContext);
            }
            case trigger_statistics_info -> {
                msg_body = Parser.parse(Msg_body_trigger_statistics_info.class, data, parentContext);
            }
            case queue_statistics_info -> {
                msg_body = Parser.parse(Msg_body_queue_statistics_info.class, data, parentContext);
            }
            case event_info -> {
                msg_body = Parser.parse(Msg_body_event_info.class, data, parentContext);
            }
            case road_info -> {
                msg_body = Parser.parse(Msg_body_road_info.class, data, parentContext);
            }
            default -> {
                throw BaseRuntimeException.getException("frame_type[{}] not support", msg.msg_header.frame_type);
            }
        }
        return msg_body;
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext parentContext, Msg_body instance) {
        Parser.deParse(instance, data, parentContext);
    }

    public static void main(String[] args) {
        System.out.println("["+new String(new byte[1])+"]");
    }
}
