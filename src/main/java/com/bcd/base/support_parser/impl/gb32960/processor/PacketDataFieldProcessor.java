package com.bcd.base.support_parser.impl.gb32960.processor;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.builder.FieldBuilder__F_date_bytes_6;
import com.bcd.base.support_parser.impl.gb32960.data.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import com.bcd.base.util.DateZoneUtil;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


public class PacketDataFieldProcessor implements Processor<PacketData> {

    final Processor<VehicleLoginData> processor_vehicleLoginData = Parser.getProcessor(VehicleLoginData.class);
    final Processor<VehicleLogoutData> processor_vehicleLogoutData = Parser.getProcessor(VehicleLogoutData.class);
    final Processor<PlatformLoginData> processor_platformLoginData = Parser.getProcessor(PlatformLoginData.class);
    final Processor<PlatformLogoutData> processor_platformLogoutData = Parser.getProcessor(PlatformLogoutData.class);

    final Processor<VehicleBaseData> processor_vehicleBaseData = Parser.getProcessor(VehicleBaseData.class);
    final Processor<VehicleMotorData> processor_vehicleMotorData = Parser.getProcessor(VehicleMotorData.class);
    final Processor<VehicleFuelBatteryData> processor_vehicleFuelBatteryData = Parser.getProcessor(VehicleFuelBatteryData.class);
    final Processor<VehicleEngineData> processor_vehicleEngineData = Parser.getProcessor(VehicleEngineData.class);
    final Processor<VehiclePositionData> processor_vehiclePositionData = Parser.getProcessor(VehiclePositionData.class);
    final Processor<VehicleLimitValueData> processor_vehicleLimitValueData = Parser.getProcessor(VehicleLimitValueData.class);
    final Processor<VehicleAlarmData> processor_vehicleAlarmData = Parser.getProcessor(VehicleAlarmData.class);
    final Processor<VehicleStorageVoltageData> processor_vehicleStorageVoltageData = Parser.getProcessor(VehicleStorageVoltageData.class);
    final Processor<VehicleStorageTemperatureData> processor_vehicleStorageTemperatureData = Parser.getProcessor(VehicleStorageTemperatureData.class);

    static Logger logger = LoggerFactory.getLogger(PacketDataFieldProcessor.class);

    @Override
    public final PacketData process(final ByteBuf data, final ProcessContext<?> processContext) {
        final Packet packet = (Packet) processContext.instance;
        if (packet.replyFlag == 0xfe) {
            PacketData packetData = null;
            switch (packet.flag) {
                //车辆登入
                case vehicle_login_data -> {
                    packetData = processor_vehicleLoginData.process(data, null);
                }

                //车辆实时信息
                //补发信息上报
                case vehicle_run_data, vehicle_supplement_data -> {
                    packetData = read_vehicleRunData(data, processContext);
                }

                //车辆登出
                case vehicle_logout_data -> {
                    packetData = processor_vehicleLogoutData.process(data, null);
                }

                //平台登入
                case platform_login_data -> {
                    packetData = processor_platformLoginData.process(data, null);
                }

                //平台登出
                case platform_logout_data -> {
                    packetData = processor_platformLogoutData.process(data, null);
                }

                //心跳
                //终端校时
                case heartbeat, terminal_timing -> {
                }
            }
            return packetData;
        } else {
            final ResponseData responseData = new ResponseData();
            responseData.content = new byte[packet.contentLength];
            data.readBytes(responseData.content);
            return responseData;
        }
    }

    @Override
    public final void deProcess(ByteBuf data, ProcessContext<?> processContext, PacketData instance) {
        final Packet packet = (Packet) processContext.instance;
        if (packet.replyFlag == 0xfe) {
            switch (packet.flag) {
                //车辆登入
                case vehicle_login_data -> {
                    processor_vehicleLoginData.deProcess(data, null, (VehicleLoginData) instance);
                }
                //车辆实时信息
                //补发信息上报
                case vehicle_run_data, vehicle_supplement_data -> {
                    write_vehicleRunData(data, (VehicleRunData) instance, processContext);
                }
                //车辆登出
                case vehicle_logout_data -> {
                    processor_vehicleLogoutData.deProcess(data, null, (VehicleLogoutData) instance);
                }

                //平台登入
                case platform_login_data -> {
                    processor_platformLoginData.deProcess(data, null, (PlatformLoginData) instance);
                }

                //平台登出
                case platform_logout_data -> {
                    processor_platformLogoutData.deProcess(data, null, (PlatformLogoutData) instance);
                }

                //心跳
                //终端校时
                case heartbeat, terminal_timing -> {
                }
            }
        } else {
            ((ResponseData) instance).content = new byte[packet.contentLength];
            data.writeBytes(((ResponseData) instance).content);
        }
    }


    private VehicleRunData read_vehicleRunData(ByteBuf data, ProcessContext<?> parentContext) {
        VehicleRunData instance = new VehicleRunData();
        instance.collectTime = new Date(FieldBuilder__F_date_bytes_6.read(data, DateZoneUtil.ZONE_OFFSET, 2000));
        final Packet packet = (Packet) parentContext.instance;
        int allLen = packet.contentLength - 6;
        int beginLeave = data.readableBytes();
        A:
        while (data.isReadable()) {
            int curLeave = data.readableBytes();
            if (beginLeave - curLeave >= allLen) {
                break;
            }
            short flag = data.readUnsignedByte();
            switch (flag) {
                case 1 -> {
                    //2.1、整车数据
                    instance.vehicleBaseData = processor_vehicleBaseData.process(data, parentContext);
                }
                case 2 -> {
                    //2.2、驱动电机数据
                    instance.vehicleMotorData = processor_vehicleMotorData.process(data, parentContext);
                }
                case 3 -> {
                    //2.3、燃料电池数据
                    instance.vehicleFuelBatteryData = processor_vehicleFuelBatteryData.process(data, parentContext);
                }
                case 4 -> {
                    //2.4、发动机数据
                    instance.vehicleEngineData = processor_vehicleEngineData.process(data, parentContext);
                }
                case 5 -> {
                    //2.5、车辆位置数据
                    instance.vehiclePositionData = processor_vehiclePositionData.process(data, parentContext);
                }
                case 6 -> {
                    //2.6、极值数据
                    instance.vehicleLimitValueData = processor_vehicleLimitValueData.process(data, parentContext);
                }
                case 7 -> {
                    //2.7、报警数据
                    instance.vehicleAlarmData = processor_vehicleAlarmData.process(data, parentContext);
                }
                case 8 -> {
                    //2.8、可充电储能装置电压数据
                    instance.vehicleStorageVoltageData = processor_vehicleStorageVoltageData.process(data, parentContext);
                }
                case 9 -> {
                    //2.9、可充电储能装置温度数据
                    instance.vehicleStorageTemperatureData = processor_vehicleStorageTemperatureData.process(data, parentContext);
                }
                default -> {
                    logger.warn("flag[" + flag + "] not support");
                    //2.8、如果是自定义数据,只做展现,不解析
                    //2.8.1、解析长度
                    int dataLen = data.readUnsignedShort();
                    //2.8.2、获取接下来的报文
                    byte[] content = new byte[dataLen];
                    data.getBytes(0, content);
                    break A;
                }
            }
        }
        return instance;
    }

    private void write_vehicleRunData(ByteBuf data, VehicleRunData instance, ProcessContext<?> parentContext) {
        FieldBuilder__F_date_bytes_6.write(data, instance.collectTime.getTime(), DateZoneUtil.ZONE_OFFSET, 2000);
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
