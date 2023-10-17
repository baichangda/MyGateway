package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.util.DateZoneUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

public class SetPolygonArea implements PacketBody {
    //区域id
    public long id;
    //区域属性
    public short attr;
    //起始时间
    public Date startTime;
    //结束时间
    public Date endTime;
    //最高速度
    public int speed;
    //超速持续时间
    public short duration;
    //区域总顶点数
    public int num;
    //顶点项
    public PolygonAreaItem[] items;
    //夜间最高速度
    public int nightSpeed;
    //名称长度
    public int nameLen;
    //名称
    public String name;

    public static SetPolygonArea read(ByteBuf data) {
        SetPolygonArea setPolygonArea = new SetPolygonArea();
        setPolygonArea.id = data.readUnsignedInt();
        short attr = data.readShort();
        setPolygonArea.attr = attr;
        if ((attr & 0x01) != 0) {
            setPolygonArea.startTime = Date.from(LocalDateTime.of(data.readByte() + 2000, data.readByte(), data.readByte(), data.readByte(), data.readByte(), data.readByte()).toInstant(DateZoneUtil.ZONE_OFFSET));
            setPolygonArea.endTime = Date.from(LocalDateTime.of(data.readByte() + 2000, data.readByte(), data.readByte(), data.readByte(), data.readByte(), data.readByte()).toInstant(DateZoneUtil.ZONE_OFFSET));
        }
        if (((attr >> 1) & 0x01) != 0) {
            setPolygonArea.speed = data.readUnsignedShort();
            setPolygonArea.duration = data.readUnsignedByte();
        }
        int num = data.readUnsignedShort();
        setPolygonArea.num = num;
        PolygonAreaItem[] items = new PolygonAreaItem[num];
        setPolygonArea.items = items;
        for (int i = 0; i < num; i++) {
            items[i] = PolygonAreaItem.read(data);
        }
        if (((attr >> 1) & 0x01) != 0) {
            setPolygonArea.nightSpeed = data.readUnsignedShort();
        }
        setPolygonArea.nameLen = data.readUnsignedShort();
        setPolygonArea.name = data.readCharSequence(setPolygonArea.nameLen, StandardCharsets.UTF_8).toString();
        return setPolygonArea;
    }
}
