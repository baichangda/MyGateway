package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.util.DateZoneUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

public class CircleAreaItem implements AreaOrPathItem {
    //区域id
    public long id;
    //区域属性
    public short attr;
    //中心点纬度
    public double lat;
    //中心点经度
    public double lng;
    //半径
    public long radius;
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

    public static CircleAreaItem read(ByteBuf data) {
        CircleAreaItem item = new CircleAreaItem();
        item.id = data.readUnsignedInt();
        short attr = data.readShort();
        item.attr = attr;
        item.lat = data.readUnsignedInt() / 1000000d;
        item.lng = data.readUnsignedInt() / 1000000d;
        item.radius = data.readUnsignedInt();
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
