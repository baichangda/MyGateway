package com.bcd.base.support_parser.impl.icd.processor;

import com.bcd.base.exception.MyException;
import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.impl.icd.data.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;

public class Target_info_extras_processor implements Processor<Target_info_extras> {

    final Processor<Target_info_extras_person> processor_target_info_extras_person = Parser.getProcessor(Target_info_extras_person.class);
    final Processor<Target_info_extras_car> processor_target_info_extras_car = Parser.getProcessor(Target_info_extras_car.class);

    @Override
    public Target_info_extras process(ByteBuf data, ProcessContext parentContext) {
        TargetClass targetClass;
        if (parentContext.instance instanceof Target_info target_info) {
            targetClass = target_info.clazz;
        } else if (parentContext.instance instanceof Event_target event_target) {
            targetClass = event_target.targetClass;
        } else {
            throw MyException.get("parentContext.instance[{}] not support", parentContext.instance.getClass());
        }
        if (targetClass == null) {
            return null;
        }
        final Target_info_extras extras;
        switch (targetClass) {
            case person -> {
                extras = processor_target_info_extras_person.process(data, parentContext);
            }
            case car -> {
                extras = processor_target_info_extras_car.process(data, parentContext);
            }
            default -> {
                throw MyException.get("sensor_type[{}] not support", targetClass);
            }
        }
        return extras;
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext parentContext, Target_info_extras instance) {
        TargetClass targetClass;
        if (parentContext.instance instanceof Target_info target_info) {
            targetClass = target_info.clazz;
        } else if (parentContext.instance instanceof Event_target event_target) {
            targetClass = event_target.targetClass;
        } else {
            throw MyException.get("parentContext.instance[{}] not support", parentContext.instance.getClass());
        }
        if (targetClass == null) {
            return;
        }
        switch (targetClass) {
            case person -> {
                processor_target_info_extras_person.deProcess(data, parentContext, (Target_info_extras_person) instance);
            }
            case car -> {
                processor_target_info_extras_car.deProcess(data, parentContext, (Target_info_extras_car) instance);
            }
            default -> {
                throw MyException.get("sensor_type[{}] not support", targetClass);
            }
        }
    }
}
