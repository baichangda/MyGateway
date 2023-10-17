package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.util.DateZoneUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

public class RectangleAreaItem  implements AreaOrPathItem {
    //区域id
    public long id;
    //区域属性
    public short attr;
    //左上点纬度
    public double lat1;
    //左上点经度
    public double lng1;
    //右下点纬度
    public double lat2;
    //右下点经度
    public double lng2;
    //起始时间
    public Date startTime;
    //结束时间
    public Date endTime;
    //最高速度
    public int speed;
    //超速持续时间
    public short duration;
    //夜间最高速度
    public int nightSpeed;
    //名称长度
    public int nameLen;
    //名称
    public String name;

    public static RectangleAreaItem read(ByteBuf data){
        RectangleAreaItem item = new RectangleAreaItem();
        item.id = data.readUnsignedInt();
        short attr = data.readShort();
        item.attr = attr;
        item.lat1 = data.readUnsignedInt() / 1000000d;
        item.lng1 = data.readUnsignedInt() / 1000000d;
        item.lat2 = data.readUnsignedInt() / 1000000d;
        item.lng2 = data.readUnsignedInt() / 1000000d;
        if ((attr & 0x01) != 0) {
            item.startTime = Date.from(LocalDateTime.of(data.readByte() + 2000, data.readByte(), data.readByte(), data.readByte(), data.readByte(), data.readByte()).toInstant(DateZoneUtil.ZONE_OFFSET));
            item.endTime = Date.from(LocalDateTime.of(data.readByte() + 2000, data.readByte(), data.readByte(), data.readByte(), data.readByte(), data.readByte()).toInstant(DateZoneUtil.ZONE_OFFSET));
        }
        if (((attr >> 1) & 0x01) != 0) {
            item.speed = data.readUnsignedShort();
            item.duration = data.readUnsignedByte();
            item.nightSpeed = data.readUnsignedShort();
        }
        item.nameLen = data.readUnsignedShort();
        item.name = data.readCharSequence(item.nameLen, StandardCharsets.UTF_8).toString();
        return item;
    }
}
