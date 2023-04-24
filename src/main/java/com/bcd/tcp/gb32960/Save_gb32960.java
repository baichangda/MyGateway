package com.bcd.tcp.gb32960;

import com.bcd.base.support_parser.impl.gb32960.data.Packet;
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
public class Save_gb32960 implements ApplicationListener<ContextRefreshedEvent> {

    static Logger logger= LoggerFactory.getLogger(Save_gb32960.class);

    public final ConcurrentLinkedQueue<Packet> queue = new ConcurrentLinkedQueue<>();
    public final ExecutorService pool = Executors.newSingleThreadExecutor();
    public final ArrayList<SaveData_gb32960> cache = new ArrayList<>();

    @Autowired
    public MongoTemplate mongoTemplate;

    public void start() {
        pool.execute(() -> {
            long prevTs = System.currentTimeMillis();
            while (true) {
                final Packet poll = queue.poll();
                if (poll == null) {
                    //没有数据休眠100ms
                    ExecutorUtil.sleep_ms(100);
                } else {
                    cache.add(SaveData_gb32960.from(poll));
                }
                final long curTs = System.currentTimeMillis();
                if (curTs - prevTs > 1000) {
                    try {
                        if (!cache.isEmpty()) {
                            Helper_gb32960.saveBatch(cache);
                        }
                    }catch (Exception ex){
                        logger.error("error",ex);
                    }finally {
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
