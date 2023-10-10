package com.bcd.root.data.gb32960;

import com.bcd.root.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.netty.buffer.ByteBuf;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

@Document("data_gb32960")
public class SaveData {
    /**
     * vin_hash(长度4)
     * vin(长度17)
     * 类型flag 16进制(长度2)
     * 时间(长度14、20220101010101)
     */
    @Id
    public String id;

    @Transient
    public JsonData jsonData;

    @JsonIgnore
    public String json;

    public static SaveData readSaveData(ByteBuf byteBuf) {
        SaveData saveData = new SaveData();
        JsonData temp = JsonData.readJsonData(byteBuf);
        saveData.jsonData = temp;
        saveData.id = Helper.toId(temp.vin, temp.type, temp.collectTime);
        saveData.json = JsonUtil.toJson(temp);
        return saveData;
    }

    public Update toUpdate() {
        return Update.update("json", json);
    }
}
