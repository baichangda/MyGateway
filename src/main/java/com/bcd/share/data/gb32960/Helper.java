package com.bcd.share.data.gb32960;

import com.bcd.share.exception.BaseRuntimeException;
import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.impl.gb32960.data.Packet;
import com.bcd.share.support_parser.processor.Processor;
import com.bcd.share.util.DateZoneUtil;
import com.bcd.share.util.JsonUtil;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    static final Processor<Packet> processor_packet;

    static {
        processor_packet = Parser.getProcessor(Packet.class);
    }

    public static int getDbIndex(String vin, int dbNum) {
        return Math.floorMod(vin.hashCode(), dbNum);
    }

    public static void saveBatch(MongoTemplate mongoTemplate, List<SaveData> list) {
        if (list.isEmpty()) {
            return;
        }
        final List<Pair<Query, Update>> collect = list.stream()
                .map(e -> Pair.of(Query.query(Criteria.where("id").is(e.id)), e.toUpdate()))
                .collect(Collectors.toList());
        mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, SaveData.class)
                .upsert(collect).execute();
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
            for (SaveData d : list) {
                d.jsonData = JsonUtil.GLOBAL_OBJECT_MAPPER.readValue(d.json, JsonData.class);
                d.json = null;
            }
        } catch (IOException ex) {
            throw BaseRuntimeException.getException(ex);
        }
        return list;
    }

    public static String toId(String vin, byte type, Date collectTime) {
        return Hashing.md5().hashString(vin, StandardCharsets.UTF_8).toString().substring(0, 4)
                + Strings.padEnd(vin, 17, '#')
                + Strings.padStart(Integer.toHexString(type), 2, '0')
                + DateZoneUtil.dateToString_second(collectTime);
    }

    public static String toId_max(String vin, byte type) {
        return Hashing.md5().hashString(vin, StandardCharsets.UTF_8).toString().substring(0, 4)
                + Strings.padEnd(vin, 17, '#')
                + Strings.padStart(Integer.toHexString(type), 2, '0')
                + "99999999999999";
    }

    public static String toId_min(String vin, byte type) {
        return Hashing.md5().hashString(vin, StandardCharsets.UTF_8).toString().substring(0, 4)
                + Strings.padEnd(vin, 17, '#')
                + Strings.padStart(Integer.toHexString(type), 2, '0')
                + "00000000000000";
    }

    public static Packet toPacket(SaveData saveData) {
        byte[] bytes = ByteBufUtil.decodeHexDump(saveData.jsonData.hex);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        return processor_packet.process(byteBuf, null);
    }
}
