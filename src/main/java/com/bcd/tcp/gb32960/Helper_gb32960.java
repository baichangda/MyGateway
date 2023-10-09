package com.bcd.tcp.gb32960;

import com.bcd.base.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.gb32960.data.Packet;
import com.bcd.base.support_parser.impl.gb32960.data.PacketFlag;
import com.bcd.base.util.JsonUtil;
import io.netty.buffer.ByteBuf;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Helper_gb32960 {

    public static int getDbIndex(String vin, int dbNum) {
        return Math.floorMod(vin.hashCode(), dbNum);
    }

    public static void saveBatch(MongoTemplate mongoTemplate, List<SaveData_gb32960> list) {
        if (list.isEmpty()) {
            return;
        }
        final List<Pair<Query, Update>> collect = list.stream()
                .map(e -> Pair.of(Query.query(Criteria.where("id").is(e.id)), e.toUpdate()))
                .collect(Collectors.toList());
        mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Save_gb32960.class)
                .upsert(collect).execute();
    }

    public static List<SaveData_gb32960> list(MongoTemplate mongoTemplate, String vin, PacketFlag flag, Date beginTime, Date endTime, int skip, int limit, boolean desc) {
        Query query = new Query();
        query.skip(skip);
        query.limit(limit);
        query.with(Sort.by(desc ? Sort.Direction.DESC : Sort.Direction.ASC, "id"));
        final Criteria criteria = Criteria.where("id");
        if (beginTime == null) {
            criteria.gte(SaveData_gb32960.toId_min(vin, flag));
        } else {
            criteria.gte(SaveData_gb32960.toId(vin, flag, beginTime));
        }
        if (endTime == null) {
            criteria.lt(SaveData_gb32960.toId_max(vin, flag));
        } else {
            criteria.lt(SaveData_gb32960.toId(vin, flag, endTime));
        }
        query.addCriteria(criteria);
        final List<SaveData_gb32960> list = mongoTemplate.find(query, SaveData_gb32960.class);
        try {
            for (SaveData_gb32960 d : list) {
                d.packet = JsonUtil.GLOBAL_OBJECT_MAPPER.readValue(d.json, Packet.class);
                d.json = null;
            }
        } catch (IOException ex) {
            throw BaseRuntimeException.getException(ex);
        }
        return list;
    }
}
