package com.bcd.share.support_parser.impl.jtt808.data;


import com.bcd.share.util.DateZoneUtil;
import io.netty.buffer.ByteBuf;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Position implements PacketBody {
    //报警标志
    public long flag;
    //状态
    public long status;
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

    public PositionExt[] exts;

    public static Position read(ByteBuf data, int len) {
        Position position = new Position();
        position.flag = data.readUnsignedInt();
        position.status = data.readUnsignedInt();
        position.lat = data.readUnsignedInt() / 1000000d;
        position.lng = data.readUnsignedInt() / 1000000d;
        position.alt = data.readUnsignedShort();
        position.speed = data.readUnsignedShort() / 10f;
        position.direction = data.readUnsignedShort();
        position.time = Date.from(LocalDateTime.of(data.readUnsignedByte() + 2000, data.readByte(), data.readByte(), data.readByte(), data.readByte(), data.readByte()).toInstant(DateZoneUtil.ZONE_OFFSET));
        int byteLen = len - 22;
        int readerIndex = data.readerIndex();
        ArrayList<PositionExt> list = new ArrayList<>();
        while (byteLen > data.readerIndex() - readerIndex) {
            list.add(PositionExt.read(data));
        }
        position.exts = list.toArray(new PositionExt[0]);
        return position;
    }
}
