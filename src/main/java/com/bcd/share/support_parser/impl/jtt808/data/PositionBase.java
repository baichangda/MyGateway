package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.support_parser.builder.FieldBuilder__F_date_bcd;
import com.bcd.share.util.DateZoneUtil;
import io.netty.buffer.ByteBuf;

import java.util.Date;

public class PositionBase {
    //报警标志
    public int flag;
    //状态
    public int status;
    //维度
    public double lat;
    //经度
    public double lng;
    //高程
    public int alt;
    //速度
    public float speed;
    //方向
    public int direction;
    //时间
    public Date time;

    public static PositionBase read(ByteBuf data) {
        PositionBase positionBase = new PositionBase();
        positionBase.flag = data.readInt();
        positionBase.status = data.readInt();
        positionBase.lat = data.readUnsignedInt() / 1000000d;
        positionBase.lng = data.readUnsignedInt() / 1000000d;
        positionBase.alt = data.readUnsignedShort();
        positionBase.speed = data.readUnsignedShort() / 10f;
        positionBase.direction = data.readUnsignedShort();
        positionBase.time = new Date(FieldBuilder__F_date_bcd.read(data, DateZoneUtil.ZONE_OFFSET, 1990));
        return positionBase;
    }
}
