package com.bcd.tcp.icd;

import com.bcd.base.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.icd.data.FrameType;
import com.bcd.base.support_parser.impl.icd.data.Msg;
import com.bcd.base.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Helper_icd {
    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        Helper_icd.mongoTemplate = mongoTemplate;
    }

    public static MongoTemplate mongoTemplate;

    public static void saveBatch(List<SaveData_icd> list) {
        if (list.isEmpty()) {
            return;
        }
        final List<Pair<Query, Update>> collect = list.stream()
                .map(e -> Pair.of(Query.query(Criteria.where("id").is(e.id)), e.toUpdate()))
                .collect(Collectors.toList());
        mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, SaveData_icd.class)
                .upsert(collect).execute();
    }

    public static List<SaveData_icd> list(long deviceSn, FrameType frameType, Date beginTime, Date endTime, int skip, int limit, boolean desc) {
        Query query = new Query();
        query.skip(skip);
        query.limit(limit);
        query.with(Sort.by(desc ? Sort.Direction.DESC : Sort.Direction.ASC, "id"));
        final Criteria criteria = Criteria.where("id");
        if (beginTime == null) {
            criteria.gte(SaveData_icd.toId_min(deviceSn, frameType));
        } else {
            criteria.gte(SaveData_icd.toId(deviceSn, frameType, beginTime));
        }
        if (endTime == null) {
            criteria.lt(SaveData_icd.toId_max(deviceSn, frameType));
        } else {
            criteria.lt(SaveData_icd.toId(deviceSn, frameType, endTime));
        }
        query.addCriteria(criteria);
        final List<SaveData_icd> list = mongoTemplate.find(query, SaveData_icd.class);
        try {
            for (SaveData_icd d : list) {
                d.msg = JsonUtil.GLOBAL_OBJECT_MAPPER.readValue(d.json, Msg.class);
                d.json = null;
            }
        } catch (IOException ex) {
            throw BaseRuntimeException.getException(ex);
        }
        return list;
    }
}
