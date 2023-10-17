package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.util.DateZoneUtil;
import io.netty.buffer.ByteBuf;

import java.time.LocalDateTime;
import java.util.Date;

public class SetPath implements PacketBody{
    //路线id
    public long id;
    //路线属性
    public short attr;
    //起始时间
    public Date startTime;
    //结束时间
    public Date endTime;
    //路线拐点总数
    public int num;
    //拐点项
    public CornerItem[] items;
    //名称长度
    public int nameLen;
    //路线名称
    public String name;

    public static SetPath read(ByteBuf data){
        SetPath setPath = new SetPath();
        setPath.id = data.readUnsignedInt();
        short attr = data.readShort();
        setPath.attr = attr;
        if ((attr & 0x01) != 0) {
            setPath.startTime = Date.from(LocalDateTime.of(data.readByte() + 2000, data.readByte(), data.readByte(), data.readByte(), data.readByte(), data.readByte()).toInstant(DateZoneUtil.ZONE_OFFSET));
            setPath.endTime = Date.from(LocalDateTime.of(data.readByte() + 2000, data.readByte(), data.readByte(), data.readByte(), data.readByte(), data.readByte()).toInstant(DateZoneUtil.ZONE_OFFSET));
        }
        int num = data.readUnsignedShort();
        setPath.num = num;
        CornerItem[] items = new CornerItem[num];
        setPath.items = items;
        for (int i = 0; i < num; i++) {
            items[i] = CornerItem.read(data);
        }
        return setPath;
    }
}
