package com.bcd.tcp.icd;

import com.bcd.base.support_parser.impl.icd.data.FrameType;
import com.bcd.base.support_parser.impl.icd.data.Msg;
import com.bcd.base.support_parser.impl.icd.data.Msg_header;
import com.bcd.base.util.DateZoneUtil;
import com.bcd.base.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Document("data_icd")
public class SaveData_icd {
    /**
     * 设备号device_sn16进制hash(长度4)
     * 设备号device_sn16进制(长度8、不够前面补0)、视为4字节
     * 类型frame_type16进制(长度4、不够前面补0)
     * 时间(20220101010101000)、到毫秒
     */
    @Id
    public String id;

    @JsonIgnore
    public String json;

    @Transient
    public Msg msg;

    public SaveData_icd(String id, Msg msg) {
        this.id = id;
        this.msg = msg;
        this.json = JsonUtil.toJson(msg);
    }

    public SaveData_icd() {
    }

    public Update toUpdate() {
        return Update.update("json", json);
    }

    public static String toId(long deviceSn, FrameType frameType, Date collectTime) {
        final String id = Integer.toHexString((int) deviceSn);
        final String collectTimeStr = DateZoneUtil.dateToString_millisecond(collectTime);
        return Hashing.md5().hashString(id, StandardCharsets.UTF_8).toString().substring(0, 4)
                + Strings.padStart(id, 8, '0')
                + Strings.padStart(Integer.toHexString(frameType.type), 4, '0')
                + collectTimeStr;
    }

    public static String toId_max(long deviceSn, FrameType frameType) {
        final String id = Integer.toHexString((int) deviceSn);
        return Hashing.md5().hashString(id, StandardCharsets.UTF_8).toString().substring(0, 4)
                + Strings.padStart(id, 8, '0')
                + Strings.padStart(Integer.toHexString(frameType.type), 4, '0')
                + "99999999999999999";
    }

    public static String toId_min(long deviceSn, FrameType frameType) {
        final String id = Integer.toHexString((int) deviceSn);
        return Hashing.md5().hashString(id, StandardCharsets.UTF_8).toString().substring(0, 4)
                + Strings.padStart(id, 8, '0')
                + Strings.padStart(Integer.toHexString(frameType.type), 4, '0')
                + "00000000000000000";
    }

    public static SaveData_icd from(Msg msg) {
        final Msg_header msg_header = msg.msg_header;
        final Date collectTime = msg_header.timestamp;
        return new SaveData_icd(toId(msg_header.device_sn, msg_header.frame_type, collectTime), msg);
    }

    public static void main(String[] args) throws InterruptedException {
        final HashFunction hashFunction = Hashing.md5();
        System.out.println(hashFunction.hashString("2", StandardCharsets.UTF_8));
        System.out.println(hashFunction.hashString("2", StandardCharsets.UTF_8));

        System.out.println(Long.toHexString(1L));
        System.out.println(Strings.padStart(Long.toHexString(1L), 16, '0'));
        System.out.println(Long.valueOf(Strings.padStart(Long.toHexString(1L), 16, '0'), 16));

        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeDoubleLE(1681812521.428);
        System.out.println(ByteBufUtil.hexDump(byteBuf));

    }
}
