package com.bcd.share.support_parser.impl.jtt808.processor;

import com.bcd.share.exception.BaseRuntimeException;
import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.impl.jtt808.data.*;
import com.bcd.share.support_parser.processor.ProcessContext;
import com.bcd.share.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;

public class PacketBodyProcessor implements Processor<PacketBody> {
    Processor<TerminalCommonResponse> processor_TerminalCommonResponse = Parser.getProcessor(TerminalCommonResponse.class);

    Processor<PlatformCommonResponse> processor_PlatformCommonResponse = Parser.getProcessor(PlatformCommonResponse.class);

    Processor<QueryServerTimeResponse> processor_QueryServerTimeResponse = Parser.getProcessor(QueryServerTimeResponse.class);

    Processor<ServerSubPacketRequest> processor_ServerSubPacketRequest = Parser.getProcessor(ServerSubPacketRequest.class);

    Processor<TerminalRegisterRequest> processor_TerminalRegisterRequest = Parser.getProcessor(TerminalRegisterRequest.class);

    Processor<TerminalAuthentication> processor_TerminalAuthentication = Parser.getProcessor(TerminalAuthentication.class);

    Processor<SetTerminalParam> processor_SetTerminalParam = Parser.getProcessor(SetTerminalParam.class);

    Processor<QueryTerminalParamRequest> processor_QueryTerminalParamRequest = Parser.getProcessor(QueryTerminalParamRequest.class);

    Processor<QueryTerminalParamResponse> processor_QueryTerminalParamResponse = Parser.getProcessor(QueryTerminalParamResponse.class);

    Processor<QueryTerminalPropResponse> processor_QueryTerminalPropResponse = Parser.getProcessor(QueryTerminalPropResponse.class);

    Processor<IssuedTerminalUpgradeRequest> processor_IssuedTerminalUpgradeRequest = Parser.getProcessor(IssuedTerminalUpgradeRequest.class);

    Processor<TerminalUpgradeResResponse> processor_TerminalUpgradeResResponse = Parser.getProcessor(TerminalUpgradeResResponse.class);
    Processor<TempPositionFollow> processor_TempPositionFollow = Parser.getProcessor(TempPositionFollow.class);
    Processor<ConfirmAlarmMsg> processor_ConfirmAlarmMsg = Parser.getProcessor(ConfirmAlarmMsg.class);
    Processor<SetPhoneText> processor_SetPhoneText = Parser.getProcessor(SetPhoneText.class);
    Processor<VehicleControlRequest> processor_VehicleControlRequest = Parser.getProcessor(VehicleControlRequest.class);
    Processor<DeleteCircleArea> processor_DeleteCircleArea = Parser.getProcessor(DeleteCircleArea.class);
    Processor<DeleteRectangleArea> processor_DeleteRectangleArea = Parser.getProcessor(DeleteRectangleArea.class);
    Processor<DeletePolygonArea> processor_DeletePolygonArea = Parser.getProcessor(DeletePolygonArea.class);
    Processor<DeletePath> processor_DeletePath = Parser.getProcessor(DeletePath.class);
    Processor<QueryAreaOrPathRequest> processor_QueryAreaOrPathRequest = Parser.getProcessor(QueryAreaOrPathRequest.class);
    Processor<WaybillReport> processor_WaybillReport = Parser.getProcessor(WaybillReport.class);
    Processor<DriverIdentityReport> processor_DriverIdentityReport = Parser.getProcessor(DriverIdentityReport.class);
    Processor<CanDataUpload> processor_CanDataUpload = Parser.getProcessor(CanDataUpload.class);


    @Override
    public PacketBody process(ByteBuf data, ProcessContext<?> parentContext) {
        Packet packet = (Packet) parentContext.instance;
        PacketBody packetBody;
        switch (packet.header.msgId) {
            case 0x0001 -> {
                packetBody = processor_TerminalCommonResponse.process(data, parentContext);
            }
            case 0x8001 -> {
                packetBody = processor_PlatformCommonResponse.process(data, parentContext);
            }
            case 0x0002, 0x0004, 0x0003, 0x8104, 0x8107, 0x8201, 0x8204, 0x8702 -> {
                packetBody = null;
            }
            case 0x8004 -> {
                packetBody = processor_QueryServerTimeResponse.process(data, parentContext);
            }
            case 0x8003, 0x0005 -> {
                packetBody = processor_ServerSubPacketRequest.process(data, parentContext);
            }
            case 0x0100 -> {
                packetBody = processor_TerminalRegisterRequest.process(data, parentContext);
            }
            case 0x8100 -> {
                packetBody = TerminalRegisterResponse.read(data, packet.header.msgLen);
            }
            case 0x0102 -> {
                packetBody = processor_TerminalAuthentication.process(data, parentContext);
            }
            case 0x8103 -> {
                packetBody = processor_SetTerminalParam.process(data, parentContext);
            }
            case 0x8106 -> {
                packetBody = processor_QueryTerminalParamRequest.process(data, parentContext);
            }
            case 0x0104 -> {
                packetBody = processor_QueryTerminalParamResponse.process(data, parentContext);
            }
            case 0x8105 -> {
                packetBody = TerminalControl.read(data, packet.header.msgLen);
            }
            case 0x0107 -> {
                packetBody = processor_QueryTerminalPropResponse.process(data, parentContext);
            }
            case 0x8108 -> {
                packetBody = processor_IssuedTerminalUpgradeRequest.process(data, parentContext);
            }
            case 0x0108 -> {
                packetBody = processor_TerminalUpgradeResResponse.process(data, parentContext);
            }
            case 0x0200 -> {
                packetBody = Position.read(data, packet.header.msgLen);
            }
            case 0x0201 -> {
                packetBody = QueryPositionResponse.read(data, packet.header.msgLen);
            }
            case 0x8202 -> {
                packetBody = processor_TempPositionFollow.process(data, parentContext);
            }
            case 0x8203 -> {
                packetBody = processor_ConfirmAlarmMsg.process(data, parentContext);
            }
            case 0x8300 -> {
                packetBody = TextInfoIssued.read(data, packet.header.msgLen);
            }
            case 0x8400 -> {
                packetBody = PhoneCallback.read(data, packet.header.msgLen);
            }
            case 0x8401 -> {
                packetBody = processor_SetPhoneText.process(data, parentContext);
            }
            case 0x8500 -> {
                packetBody = processor_VehicleControlRequest.process(data, parentContext);
            }
            case 0x0500 -> {
                packetBody = VehicleControlResponse.read(data, packet.header.msgLen);
            }
            case 0x8600 -> {
                packetBody = SetCircleArea.read(data);
            }
            case 0x8601 -> {
                packetBody = processor_DeleteCircleArea.process(data, parentContext);
            }
            case 0x8602 -> {
                packetBody = SetRectangleArea.read(data);
            }
            case 0x8603 -> {
                packetBody = processor_DeleteRectangleArea.process(data, parentContext);
            }
            case 0x8604 -> {
                packetBody = SetPolygonArea.read(data);
            }
            case 0x8605 -> {
                packetBody = processor_DeletePolygonArea.process(data, parentContext);
            }
            case 0x8606 -> {
                packetBody = SetPath.read(data);
            }
            case 0x8607 -> {
                packetBody = processor_DeletePath.process(data, parentContext);
            }
            case 0x8608 -> {
                packetBody = processor_QueryAreaOrPathRequest.process(data, parentContext);
            }
            case 0x0608 -> {
                packetBody = QueryAreaOrPathResponse.read(data);
            }
            case 0x8700 -> {
                packetBody = DrivingRecorderUpload.read(data, packet.header.msgLen);
            }
            case 0x8701 -> {
                packetBody = DrivingRecorderDownStream.read(data, packet.header.msgLen);
            }
            case 0x0701 -> {
                packetBody = processor_WaybillReport.process(data, parentContext);
            }
            case 0x0702 -> {
                packetBody = processor_DriverIdentityReport.process(data, parentContext);
            }
            case 0x0704 -> {
                packetBody = PositionDataUpload.read(data);
            }
            case 0x0705 -> {
                packetBody = processor_CanDataUpload.process(data, parentContext);
            }
            default -> throw BaseRuntimeException.getException("msgId[{}] not support", packet.header.msgId);
        }
        return packetBody;
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext<?> parentContext, PacketBody instance) {
        Packet packet = (Packet) parentContext.instance;
        switch (packet.header.msgId) {
            case 0x0001 -> {
                processor_TerminalCommonResponse.deProcess(data, parentContext, (TerminalCommonResponse) instance);
            }
            case 0x8001 -> {
                processor_PlatformCommonResponse.deProcess(data, parentContext, (PlatformCommonResponse) instance);
            }
            case 0x0002, 0x0004 -> {

            }
            case 0x8004 -> {
                processor_QueryServerTimeResponse.deProcess(data, parentContext, (QueryServerTimeResponse) instance);
            }
            case 0x8003, 0x0005 -> {
                processor_ServerSubPacketRequest.deProcess(data, parentContext, (ServerSubPacketRequest) instance);
            }
            case 0x0100 -> {
                processor_TerminalRegisterRequest.deProcess(data, parentContext, (TerminalRegisterRequest) instance);
            }
            default -> {
                throw BaseRuntimeException.getException("msgId[{}] not support", packet.header.msgId);
            }
        }
    }

}
