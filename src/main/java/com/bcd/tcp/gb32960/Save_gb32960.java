package com.bcd.tcp.gb32960;

import com.bcd.base.exception.BaseRuntimeException;
import com.bcd.base.support_mongo.MongoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class Save_gb32960 implements ApplicationListener<ContextRefreshedEvent> {
    static Logger logger = LoggerFactory.getLogger(Save_gb32960.class);
    public final MongoTemplate[] mongoTemplates;
    public final ArrayBlockingQueue<SaveData>[] queues;
    public final ExecutorService[] pools;
    public final ArrayList<SaveData>[] caches;
    public final int dbNum;

    public Save_gb32960(MongoHandler mongoHandler) {
        dbNum = mongoHandler.dbNum;
        mongoTemplates = mongoHandler.mongoTemplates;
        queues = new ArrayBlockingQueue[dbNum];
        pools = new ExecutorService[dbNum];
        caches = new ArrayList[dbNum];
        for (int i = 0; i < dbNum; i++) {
            queues[i] = new ArrayBlockingQueue<>(50000);
            pools[i] = Executors.newSingleThreadExecutor();
            caches[i] = new ArrayList<>();
        }
    }

    public void put(SaveData d) throws InterruptedException {
        queues[Helper.getDbIndex(d.jsonData.vin, dbNum)].put(d);
    }

    public void start() {
        for (int i = 0; i < pools.length; i++) {
            final MongoTemplate mongoTemplate = mongoTemplates[i];
            final ExecutorService pool = pools[i];
            final ArrayBlockingQueue<SaveData> queue = queues[i];
            final ArrayList<SaveData> cache = caches[i];
            pool.execute(() -> {
                try {
                    while (true) {
                        SaveData d = queue.take();
                        do {
                            Monitor_gb32960.queueNum.decrement();
                            cache.add(d);
                            if (cache.size() == 1000) {
                                saveBatch(mongoTemplate, cache);
                                Monitor_gb32960.saveNum.add(cache.size());
                                cache.clear();
                            }
                            d = queue.poll();
                        } while (d != null);
                        if (!cache.isEmpty()) {
                            saveBatch(mongoTemplate, cache);
                            Monitor_gb32960.saveNum.add(cache.size());
                            cache.clear();
                        }
                    }
                } catch (InterruptedException ex) {
                    throw BaseRuntimeException.get(ex);
                }
            });
        }
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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        start();
    }
}
