package com.bcd.share.data.gb32960;

import com.bcd.share.util.DateZoneUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.time.LocalDateTime;
import java.util.Date;

public class JsonData {
    public String vin;

    public byte type;
    public Date collectTime;
    public Date receiveTime;
    public String hex;

    public static JsonData readJsonData(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        JsonData jsonData = new JsonData();
        jsonData.type = bytes[2];
        jsonData.vin = new String(bytes, 4, 17);
        jsonData.collectTime = Date.from(LocalDateTime.of(bytes[24] + 2000, bytes[25], bytes[26], bytes[27], bytes[28], bytes[29]).toInstant(DateZoneUtil.ZONE_OFFSET));
        jsonData.receiveTime = new Date();
        jsonData.hex = ByteBufUtil.hexDump(bytes);
        return jsonData;
    }
}
