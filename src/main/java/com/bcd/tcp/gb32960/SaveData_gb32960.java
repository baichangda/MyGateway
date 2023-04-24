package com.bcd.tcp.gb32960;

import com.bcd.base.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.gb32960.data.*;
import com.bcd.base.util.DateZoneUtil;
import com.bcd.base.util.JsonUtil;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Document("data_gb32960")
public class SaveData_gb32960 {
    /**
     * vin_hash(长度4)
     * vin(长度20、不够结尾补齐#)
     * 时间(20220101010101)
     * 类型(长度2、不够结尾补齐#)
     */
    @Id
    public final String id;

    public final String json;

    public SaveData_gb32960(String id, String json) {
        this.id = id;
        this.json = json;
    }


    public static SaveData_gb32960 from(Packet packet) {
        final Date collectTime;
        switch (packet.flag) {
            case 1 -> collectTime = ((VehicleLoginData) packet.data).collectTime;
            case 2 -> collectTime = ((VehicleRealData) packet.data).collectTime;
            case 3 -> collectTime = ((VehicleSupplementData) packet.data).collectTime;
            case 4 -> collectTime = ((VehicleLogoutData) packet.data).collectTime;
            case 5 -> collectTime = ((PlatformLoginData) packet.data).collectTime;
            case 6 -> collectTime = ((PlatformLogoutData) packet.data).collectTime;
            default ->
                    throw BaseRuntimeException.getException("packet vin[{}] flag[{}] not support", packet.vin, packet.flag);
        }

        return new SaveData_gb32960(
                Hashing.md5().hashString(packet.vin, StandardCharsets.UTF_8).toString().substring(0, 4)
                        + Strings.padEnd(packet.vin, 20, '#')
                        + DateZoneUtil.dateToString_second(collectTime)
                        + Strings.padEnd(String.valueOf(packet.flag), 2, '#'),
                JsonUtil.toJson(packet)
        );

    }

    public static void main(String[] args) throws InterruptedException {
    }
}
