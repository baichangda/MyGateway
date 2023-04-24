package com.bcd.tcp.icd;

import com.bcd.base.support_parser.impl.icd.data.Msg;
import com.bcd.base.util.ExecutorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Save_icd implements ApplicationListener<ContextRefreshedEvent> {

    static Logger logger = LoggerFactory.getLogger(Save_icd.class);

    public final ConcurrentLinkedQueue<Msg> queue = new ConcurrentLinkedQueue<>();
    public final ExecutorService pool = Executors.newSingleThreadExecutor();
    public final ArrayList<SaveData_icd> cache = new ArrayList<>();

    @Autowired
    public MongoTemplate mongoTemplate;

    public void start() {
        pool.execute(() -> {
            long prevTs = System.currentTimeMillis();
            while (true) {
                final Msg poll = queue.poll();
                if (poll == null) {
                    //没有数据休眠100ms
                    ExecutorUtil.sleep_ms(100);
                } else {
                    cache.add(SaveData_icd.from(poll));
                }
                final long curTs = System.currentTimeMillis();
                if (curTs - prevTs > 1000) {
                    try {
                        if (!cache.isEmpty()) {
                            Helper_icd.saveBatch(cache);
                        }
                    } catch (Exception ex) {
                        logger.error("error", ex);
                    } finally {
                        cache.clear();
                        prevTs = curTs;
                    }
                }
            }
        });
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        start();
    }
}
