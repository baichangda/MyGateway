package com.bcd.base.support_parser.impl.jtt808.processor;

import com.bcd.base.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.jtt808.data.VehicleControlRequest;
import com.bcd.base.support_parser.impl.jtt808.data.VehicleControlType;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;

public class VehicleControlTypeArrProcessor implements Processor<VehicleControlType[]> {
    @Override
    public VehicleControlType[] process(ByteBuf data, ProcessContext<?> parentContext) {
        VehicleControlRequest vehicleControlRequest = (VehicleControlRequest) parentContext.instance;
        VehicleControlType[] types = new VehicleControlType[vehicleControlRequest.num];
        for (int i = 0; i < vehicleControlRequest.num; i++) {
            int id = data.readUnsignedShort();
            switch (id) {
                case 0x0001 -> {
                    VehicleControlType vehicleControlType = new VehicleControlType();
                    vehicleControlType.id = id;
                    vehicleControlType.param = new byte[]{data.readByte()};
                    types[i] = vehicleControlType;
                }
                default -> {
                    throw BaseRuntimeException.getException("VehicleControlType id[{}] not support", id);
                }
            }
        }
        if (types.length == 0) {
            return null;
        } else {
            return types;
        }
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext<?> parentContext, VehicleControlType[] instance) {
        if (instance != null && instance.length > 0) {
            for (VehicleControlType vehicleControlType : instance) {
                data.writeShort(vehicleControlType.id);
                data.writeBytes(vehicleControlType.param);
            }
        }
    }
}
