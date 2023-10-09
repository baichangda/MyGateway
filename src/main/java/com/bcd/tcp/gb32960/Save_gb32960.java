package com.bcd.tcp.gb32960;

import com.bcd.base.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.gb32960.data.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Save_gb32960 implements ApplicationListener<ContextRefreshedEvent> {
    static Logger logger = LoggerFactory.getLogger(Save_gb32960.class);
    public final MongoTemplate[] mongoTemplates;
    public final ArrayBlockingQueue<Packet>[] queues;
    public final ExecutorService[] pools;
    public final ArrayList<SaveData_gb32960>[] caches;
    public final int dbNum;

    public Save_gb32960(@Value("${mongodbs}") String[] mongodbs) {
        dbNum = mongodbs.length;
        mongoTemplates = new MongoTemplate[dbNum];
        queues = new ArrayBlockingQueue[dbNum];
        pools = new ExecutorService[dbNum];
        caches = new ArrayList[dbNum];
        for (int i = 0; i < dbNum; i++) {
            mongoTemplates[i] = new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongodbs[i]));
            queues[i] = new ArrayBlockingQueue<>(50000);
            pools[i] = Executors.newSingleThreadExecutor();
            caches[i] = new ArrayList<>();
        }
    }

    public void put(Packet p) throws InterruptedException {
        queues[Helper_gb32960.getDbIndex(p.vin, dbNum)].put(p);
    }

    public void start() {
        for (int i = 0; i < pools.length; i++) {
            final MongoTemplate mongoTemplate = mongoTemplates[i];
            final ExecutorService pool = pools[i];
            final ArrayBlockingQueue<Packet> queue = queues[i];
            final ArrayList<SaveData_gb32960> cache = caches[i];
            pool.execute(() -> {
                try {
                    while (true) {
                        Packet p = queue.take();
                        do {
                            cache.add(SaveData_gb32960.from(p));
                            if (cache.size() == 1000) {
                                Helper_gb32960.saveBatch(mongoTemplate, cache);
                                cache.clear();
                            }
                            p = queue.poll();
                        } while (p != null);
                        if (!cache.isEmpty()) {
                            Helper_gb32960.saveBatch(mongoTemplate, cache);
                            cache.clear();
                        }
                    }
                } catch (InterruptedException ex) {
                    throw BaseRuntimeException.getException(ex);
                }
            });
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        start();
    }
}
