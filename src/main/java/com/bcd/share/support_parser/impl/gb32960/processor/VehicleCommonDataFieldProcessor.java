package com.bcd.share.support_parser.impl.gb32960.processor;

import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.impl.gb32960.data.*;
import com.bcd.share.support_parser.processor.ProcessContext;
import com.bcd.share.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VehicleCommonDataFieldProcessor implements Processor<VehicleCommonData> {
    final Processor<VehicleBaseData> processor_vehicleBaseData = Parser.getProcessor(VehicleBaseData.class);
    final Processor<VehicleMotorData> processor_vehicleMotorData = Parser.getProcessor(VehicleMotorData.class);
    final Processor<VehicleFuelBatteryData> processor_vehicleFuelBatteryData = Parser.getProcessor(VehicleFuelBatteryData.class);
    final Processor<VehicleEngineData> processor_vehicleEngineData = Parser.getProcessor(VehicleEngineData.class);
    final Processor<VehiclePositionData> processor_vehiclePositionData = Parser.getProcessor(VehiclePositionData.class);
    final Processor<VehicleLimitValueData> processor_vehicleLimitValueData = Parser.getProcessor(VehicleLimitValueData.class);
    final Processor<VehicleAlarmData> processor_vehicleAlarmData = Parser.getProcessor(VehicleAlarmData.class);
    final Processor<VehicleStorageVoltageData> processor_vehicleStorageVoltageData = Parser.getProcessor(VehicleStorageVoltageData.class);
    final Processor<VehicleStorageTemperatureData> processor_vehicleStorageTemperatureData = Parser.getProcessor(VehicleStorageTemperatureData.class);
    Logger logger = LoggerFactory.getLogger(VehicleCommonDataFieldProcessor.class);

    @Override
    public VehicleCommonData process(final ByteBuf byteBuf, final ProcessContext<?> parentContext) {
        final VehicleCommonData vehicleCommonData = new VehicleCommonData();
        int allLen = ((Packet) parentContext.parentContext.instance).contentLength - 6;
        int beginLeave = byteBuf.readableBytes();
        A:
        while (byteBuf.isReadable()) {
            int curLeave = byteBuf.readableBytes();
            if (beginLeave - curLeave >= allLen) {
                break;
            }
            short flag = byteBuf.readUnsignedByte();
            switch (flag) {
                case 1: {
                    //2.1、整车数据
                    vehicleCommonData.vehicleBaseData = processor_vehicleBaseData.process(byteBuf, parentContext);
                    break;
                }
                case 2: {
                    //2.2、驱动电机数据
                    vehicleCommonData.vehicleMotorData = processor_vehicleMotorData.process(byteBuf, parentContext);
                    break;
                }
                case 3: {
                    //2.3、燃料电池数据
                    vehicleCommonData.vehicleFuelBatteryData = processor_vehicleFuelBatteryData.process(byteBuf, parentContext);
                    break;
                }
                case 4: {
                    //2.4、发动机数据
                    vehicleCommonData.vehicleEngineData = processor_vehicleEngineData.process(byteBuf, parentContext);
                    break;
                }
                case 5: {
                    //2.5、车辆位置数据
                    vehicleCommonData.vehiclePositionData = processor_vehiclePositionData.process(byteBuf, parentContext);
                    break;
                }
                case 6: {
                    //2.6、极值数据
                    vehicleCommonData.vehicleLimitValueData = processor_vehicleLimitValueData.process(byteBuf, parentContext);
                    break;
                }
                case 7: {
                    //2.7、报警数据
                    vehicleCommonData.vehicleAlarmData = processor_vehicleAlarmData.process(byteBuf, parentContext);
                    break;
                }
                case 8: {
                    //2.8、可充电储能装置电压数据
                    vehicleCommonData.vehicleStorageVoltageData = processor_vehicleStorageVoltageData.process(byteBuf, parentContext);
                    break;
                }
                case 9: {
                    //2.9、可充电储能装置温度数据
                    vehicleCommonData.vehicleStorageTemperatureData = processor_vehicleStorageTemperatureData.process(byteBuf, parentContext);
                    break;
                }
                default: {
                    logger.warn("Parse Vehicle Common Data Interrupted,Unknown Flag[" + flag + "]");
                    //2.8、如果是自定义数据,只做展现,不解析
                    //2.8.1、解析长度
                    int dataLen = byteBuf.readUnsignedShort();
                    //2.8.2、获取接下来的报文
                    byte[] content = new byte[dataLen];
                    byteBuf.getBytes(0, content);
                    break A;
//                      throw BaseRuntimeException.getException("Parse Vehicle Data Failed,Unknown Flag["+flag+"]");
                }
            }
        }
        return vehicleCommonData;
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext<?> parentContext, VehicleCommonData instance) {
        if (instance.vehicleBaseData != null) {
            data.writeByte(1);
            processor_vehicleBaseData.deProcess(data, parentContext, instance.vehicleBaseData);
        }
        if (instance.vehicleMotorData != null) {
            data.writeByte(2);
            processor_vehicleMotorData.deProcess(data, parentContext, instance.vehicleMotorData);
        }
        if (instance.vehicleFuelBatteryData != null) {
            data.writeByte(3);
            processor_vehicleFuelBatteryData.deProcess(data, parentContext, instance.vehicleFuelBatteryData);
        }
        if (instance.vehicleEngineData != null) {
            data.writeByte(4);
            processor_vehicleEngineData.deProcess(data, parentContext, instance.vehicleEngineData);
        }
        if (instance.vehiclePositionData != null) {
            data.writeByte(5);
            processor_vehiclePositionData.deProcess(data, parentContext, instance.vehiclePositionData);
        }
        if (instance.vehicleLimitValueData != null) {
            data.writeByte(6);
            processor_vehicleLimitValueData.deProcess(data, parentContext, instance.vehicleLimitValueData);
        }
        if (instance.vehicleAlarmData != null) {
            data.writeByte(7);
            processor_vehicleAlarmData.deProcess(data, parentContext, instance.vehicleAlarmData);
        }
        if (instance.vehicleStorageVoltageData != null) {
            data.writeByte(8);
            processor_vehicleStorageVoltageData.deProcess(data, parentContext, instance.vehicleStorageVoltageData);
        }
        if (instance.vehicleStorageTemperatureData != null) {
            data.writeByte(9);
            processor_vehicleStorageTemperatureData.deProcess(data, parentContext, instance.vehicleStorageTemperatureData);
        }
    }
}
