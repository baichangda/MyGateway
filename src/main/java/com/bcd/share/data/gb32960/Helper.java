package com.bcd.share.data.gb32960;

import com.bcd.share.support_mongo.MongoHandler;
import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.impl.gb32960.data.Packet;
import com.bcd.share.support_parser.processor.Processor;
import com.bcd.share.util.DateZoneUtil;
import com.bcd.share.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@ConditionalOnProperty(value = "mongodbs")
@Component("helper_gb32960")
public class Helper {
    public static MongoHandler mongoHandler;

    @Autowired
    public void setMongoHandler(MongoHandler mongoHandler) {
        Helper.mongoHandler = mongoHandler;
    }

    static final Processor<Packet> processor_packet;

    static {
        processor_packet = Parser.getProcessor(Packet.class);
    }

    public static MongoTemplate getMongoTemplate(String vin) {
        return mongoHandler.getMongoTemplate(vin);
    }

    public static int getDbIndex(String vin, int dbNum) {
        return Math.floorMod(vin.hashCode(), dbNum);
    }


    /**
     * 范围查询
     *
     * @param vin       vin
     * @param type      gb32960协议文档中的命令标识定义、可以为null {@link com.bcd.share.support_parser.impl.gb32960.data.PacketFlag}
     *                  1、车辆登入
     *                  2、实时信息上报
     *                  3、补发信息上报
     *                  4、车辆登出
     *                  5、平台登入
     *                  6、平台登出
     *                  7、心跳
     *                  8、终端校时
     * @param beginTime 开始时间 包含
     * @param endTime   结束时间 不包含
     * @param skip      跳过前多少条
     * @param limit     限制返回最大条数
     * @param desc      是否主键逆序
     * @return
     */
    public static List<SaveData> list(String vin, byte type, Date beginTime, Date endTime, int skip, int limit, boolean desc) {
        return list(getMongoTemplate(vin), vin, type, beginTime, endTime, skip, limit, desc);
    }

    /**
     * 获取单条报文
     *
     * @param vin
     * @param type
     * @param collectTime
     * @return
     */
    public static SaveData get(String vin, byte type, Date collectTime) {
        return get(getMongoTemplate(vin), vin, type, collectTime);
    }


    public static List<SaveData> list(MongoTemplate mongoTemplate, String vin, byte type, Date beginTime, Date endTime, int skip, int limit, boolean desc) {
        Query query = new Query();
        query.skip(skip);
        query.limit(limit);
        query.with(Sort.by(desc ? Sort.Direction.DESC : Sort.Direction.ASC, "id"));
        final Criteria criteria = Criteria.where("id");
        if (beginTime == null) {
            criteria.gte(toId_min(vin, type));
        } else {
            criteria.gte(toId(vin, type, beginTime));
        }
        if (endTime == null) {
            criteria.lt(toId_max(vin, type));
        } else {
            criteria.lt(toId(vin, type, endTime));
        }
        query.addCriteria(criteria);
        final List<SaveData> list = mongoTemplate.find(query, SaveData.class);
        try {
            for (SaveData saveData : list) {
                saveData.jsonData = JsonUtil.GLOBAL_OBJECT_MAPPER.readValue(saveData.json, JsonData.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static SaveData get(MongoTemplate mongoTemplate, String vin, byte type, Date collectTime) {
        return mongoTemplate.findById(toId(vin, type, collectTime), SaveData.class);
    }

    /**
     * 生成记录主键
     *
     * @param vin
     * @param type
     * @param collectTime
     * @return
     */
    public static String toId(String vin, byte type, Date collectTime) {
        return Hashing.md5().hashString(vin, StandardCharsets.UTF_8).toString().substring(0, 4)
                + Strings.padEnd(vin, 17, '#')
                + Strings.padStart(Integer.toHexString(type), 2, '0')
                + DateZoneUtil.dateToString_second(collectTime);
    }

    /**
     * 生成时间最大id用于筛选某类型数据
     *
     * @param vin
     * @param type
     * @return
     */
    public static String toId_max(String vin, byte type) {
        return Hashing.md5().hashString(vin, StandardCharsets.UTF_8).toString().substring(0, 4)
                + Strings.padEnd(vin, 17, '#')
                + Strings.padStart(Integer.toHexString(type), 2, '0')
                + "99999999999999";
    }

    /**
     * 生成时间最小id用于筛选某类型数据
     *
     * @param vin
     * @param type
     * @return
     */
    public static String toId_min(String vin, byte type) {
        return Hashing.md5().hashString(vin, StandardCharsets.UTF_8).toString().substring(0, 4)
                + Strings.padEnd(vin, 17, '#')
                + Strings.padStart(Integer.toHexString(type), 2, '0')
                + "00000000000000";
    }

    /**
     * 将保存的数据转换为gb32960格式数据
     *
     * @param saveData
     * @return
     */
    public static Packet toPacket(SaveData saveData) {
        byte[] bytes = ByteBufUtil.decodeHexDump(saveData.jsonData.hex);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        return processor_packet.process(byteBuf, null);
    }

    /**
     * 将16进制字符串数据转换为32960格式数据
     *
     * @param hex
     * @return
     */
    public static Packet toPacket(String hex) {
        byte[] bytes = ByteBufUtil.decodeHexDump(hex);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        return processor_packet.process(byteBuf, null);
    }
}
