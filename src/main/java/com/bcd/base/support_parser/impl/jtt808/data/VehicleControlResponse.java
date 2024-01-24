package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.anno.F_bean;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;
import io.netty.buffer.ByteBuf;

public class VehicleControlResponse implements PacketBody {
    @F_num(type = NumType.uint16)
    public int sn;
    @F_bean
    public Position position;

    public static VehicleControlResponse read(ByteBuf data, int len) {
        VehicleControlResponse vehicleControlResponse = new VehicleControlResponse();
        vehicleControlResponse.sn = data.readUnsignedShort();
        vehicleControlResponse.position = Position.read(data, len - 2);
        return vehicleControlResponse;
    }

    public void write(ByteBuf data){
        data.writeShort(sn);
        position.write(data);
    }
}
