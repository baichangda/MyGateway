package com.bcd.base.support_parser.impl.gb32960.processor;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.impl.gb32960.data.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;


public class PacketDataFieldProcessor implements Processor<PacketData> {

    @Override
    public PacketData process(final ByteBuf data, final ProcessContext parentContext) {
        Packet packet = (Packet) parentContext.instance;
        PacketData packetData = null;
        switch (packet.flag) {
            //车辆登入
            case vehicle_login_data: {
                packetData = Parser.parse(VehicleLoginData.class, data, parentContext);
                break;
            }

            //车辆实时信息
            case vehicle_run_data: {
                packetData = Parser.parse(VehicleRunData.class, data, parentContext);
                break;
            }

            //补发信息上报
            case vehicle_supplement_data: {
                packetData = Parser.parse(VehicleSupplementData.class, data, parentContext);
                break;
            }

            //车辆登出
            case vehicle_logout_data: {
                packetData = Parser.parse(VehicleLogoutData.class, data, parentContext);
                break;
            }

            //平台登入
            case platform_login_data: {
                packetData = Parser.parse(PlatformLoginData.class, data, parentContext);
                break;
            }

            //平台登出
            case platform_logout_data: {
                packetData = Parser.parse(PlatformLogoutData.class, data, parentContext);
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
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext parentContext, PacketData instance) {
        Parser.deParse(instance, data, parentContext);
    }
}
