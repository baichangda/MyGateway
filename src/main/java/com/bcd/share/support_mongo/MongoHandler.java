package com.bcd.share.support_mongo;

import com.bcd.share.data.gb32960.Helper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(value = "mongodbs")
@Component
public class MongoHandler {
    public final int dbNum;
    public final MongoTemplate[] mongoTemplates;

    public MongoHandler(@Value("${mongodbs}") String[] mongodbs) {
        dbNum = mongodbs.length;
        mongoTemplates = new MongoTemplate[dbNum];
        for (int i = 0; i < dbNum; i++) {
            SimpleMongoClientDatabaseFactory simpleMongoClientDatabaseFactory = new SimpleMongoClientDatabaseFactory(mongodbs[i]);
            MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(simpleMongoClientDatabaseFactory), new MongoMappingContext());
            converter.setTypeMapper(new DefaultMongoTypeMapper(null));
            mongoTemplates[i] = new MongoTemplate(simpleMongoClientDatabaseFactory, converter);
        }
    }

    public MongoTemplate getMongoTemplate(String vin) {
        return mongoTemplates[Helper.getDbIndex(vin, dbNum)];
    }
}