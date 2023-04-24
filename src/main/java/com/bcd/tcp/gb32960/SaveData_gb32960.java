package com.bcd.tcp.gb32960;

import com.bcd.base.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.gb32960.data.*;
import com.bcd.base.util.DateZoneUtil;
import com.bcd.base.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Document("data_gb32960")
public class SaveData_gb32960 {
    /**
     * vin_hash(长度4)
     * vin(长度20、不够结尾补齐#)
     * 类型flag16进制(长度4、不够前面补0)
     * 时间(20220101010101)
     */
    @Id
    public String id;

    @JsonIgnore
    public String json;

    @Transient
    public Packet packet;

    public SaveData_gb32960(String id, String json) {
        this.id = id;
        this.json = json;
    }

    public Update toUpdate() {
        return Update.update("json", json);
    }


    public static String toId(String vin, PacketFlag flag, Date collectTime) {
        return Hashing.md5().hashString(vin, StandardCharsets.UTF_8).toString().substring(0, 4)
                + Strings.padEnd(vin, 20, '#')
                + Strings.padStart(Integer.toHexString(flag.type), 2, '0')
                + DateZoneUtil.dateToString_second(collectTime);
    }

    public static String toId_max(String vin, PacketFlag flag) {
        return Hashing.md5().hashString(vin, StandardCharsets.UTF_8).toString().substring(0, 4)
                + Strings.padEnd(vin, 20, '#')
                + Strings.padStart(Integer.toHexString(flag.type), 2, '0')
                + "99999999999999999";
    }

    public static String toId_min(String vin, PacketFlag flag) {
        return Hashing.md5().hashString(vin, StandardCharsets.UTF_8).toString().substring(0, 4)
                + Strings.padEnd(vin, 20, '#')
                + Strings.padStart(Integer.toHexString(flag.type), 2, '0')
                + "00000000000000000";
    }


    public static SaveData_gb32960 from(Packet packet) {
        final Date collectTime;
        switch (packet.flag) {
            case vehicle_login_data -> collectTime = ((VehicleLoginData) packet.data).collectTime;
            case vehicle_run_data -> collectTime = ((VehicleRunData) packet.data).collectTime;
            case vehicle_supplement_data -> collectTime = ((VehicleSupplementData) packet.data).collectTime;
            case vehicle_logout_data -> collectTime = ((VehicleLogoutData) packet.data).collectTime;
            case platform_login_data -> collectTime = ((PlatformLoginData) packet.data).collectTime;
            case platform_logout_data -> collectTime = ((PlatformLogoutData) packet.data).collectTime;
            default ->
                    throw BaseRuntimeException.getException("packet vin[{}] flag[{}] not support", packet.vin, packet.flag);
        }

        return new SaveData_gb32960(toId(packet.vin, packet.flag, collectTime), JsonUtil.toJson(packet));
    }

    public static void main(String[] args) throws InterruptedException {
    }
}
