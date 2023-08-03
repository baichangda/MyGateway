package com.bcd.base.support_parser.impl.gb32960.processor;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.impl.gb32960.data.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;


public class PacketDataFieldProcessor implements Processor<PacketData> {
    final Processor<VehicleLoginData> processor_vehicleLoginData = Parser.getProcessor(VehicleLoginData.class);
    final Processor<VehicleRunData> processor_vehicleRunData = Parser.getProcessor(VehicleRunData.class);
    final Processor<VehicleSupplementData> processor_vehicleSupplementData = Parser.getProcessor(VehicleSupplementData.class);
    final Processor<VehicleLogoutData> processor_vehicleLogoutData = Parser.getProcessor(VehicleLogoutData.class);
    final Processor<PlatformLoginData> processor_platformLoginData = Parser.getProcessor(PlatformLoginData.class);
    final Processor<PlatformLogoutData> processor_platformLogoutData = Parser.getProcessor(PlatformLogoutData.class);

    @Override
    public PacketData process(final ByteBuf data, final ProcessContext parentContext) {

        final Packet packet = (Packet) parentContext.instance;
        if (packet.replyFlag == 0xfe) {
            PacketData packetData = null;
            switch (packet.flag) {
                //车辆登入
                case vehicle_login_data: {
                    packetData = processor_vehicleLoginData.process(data, parentContext);
                    break;
                }

                //车辆实时信息
                case vehicle_run_data: {
                    packetData = processor_vehicleRunData.process(data, parentContext);
                    break;
                }

                //补发信息上报
                case vehicle_supplement_data: {
                    packetData = processor_vehicleSupplementData.process(data, parentContext);
                    break;
                }

                //车辆登出
                case vehicle_logout_data: {
                    packetData = processor_vehicleLogoutData.process(data, parentContext);
                    break;
                }

                //平台登入
                case platform_login_data: {
                    packetData = processor_platformLoginData.process(data, parentContext);
                    break;
                }

                //平台登出
                case platform_logout_data: {
                    packetData = processor_platformLogoutData.process(data, parentContext);
                    break;
                }

                //心跳
                case heartbeat: {
                    break;
                }

                //终端校时
                case terminal_timing: {
                    break;
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
    public void deProcess(ByteBuf data, ProcessContext parentContext, PacketData instance) {
        final Packet packet = (Packet) parentContext.instance;
        if (packet.replyFlag == 0xfe) {
            switch (packet.flag) {
                //车辆登入
                case vehicle_login_data: {
                    processor_vehicleLoginData.deProcess(data, parentContext, (VehicleLoginData) instance);
                    break;
                }
                //车辆实时信息
                case vehicle_run_data: {
                    processor_vehicleRunData.deProcess(data, parentContext, (VehicleRunData) instance);
                    break;
                }

                //补发信息上报
                case vehicle_supplement_data: {
                    processor_vehicleSupplementData.deProcess(data, parentContext, (VehicleSupplementData) instance);
                    break;
                }

                //车辆登出
                case vehicle_logout_data: {
                    processor_vehicleLogoutData.deProcess(data, parentContext, (VehicleLogoutData) instance);
                    break;
                }

                //平台登入
                case platform_login_data: {
                    processor_platformLoginData.deProcess(data, parentContext, (PlatformLoginData) instance);
                    break;
                }

                //平台登出
                case platform_logout_data: {
                    processor_platformLogoutData.deProcess(data, parentContext, (PlatformLogoutData) instance);
                    break;
                }

                //心跳
                case heartbeat: {
                    break;
                }

                //终端校时
                case terminal_timing: {
                    break;
                }
            }
        } else {
            data.writeBytes(((ResponseData) instance).content);
        }
    }
}
