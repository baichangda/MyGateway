package com.bcd.share.support_parser.util;

import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

@SuppressWarnings("unchecked")
public class PerformanceUtil {

    private final static Logger logger = LoggerFactory.getLogger(PerformanceUtil.class);

    /**
     * 测试多线程
     *
     * @param bytes
     * @param clazz
     * @param threadNum
     * @param num
     * @param <T>
     */
    public static <T> void testMultiThreadPerformance(byte[] bytes, Class<T> clazz, int threadNum, int num, boolean parse) {
        final LongAdder count = new LongAdder();
        final ExecutorService[] pools = new ExecutorService[threadNum];
        for (int i = 0; i < pools.length; i++) {
            pools[i] = Executors.newSingleThreadExecutor();
        }
        if (parse) {
            for (int i = 0; i < pools.length; i++) {
                pools[i].execute(() -> {
                    testParse(bytes, clazz, num, count);
                });
            }
        } else {
            Processor<T> processor = Parser.getProcessor(clazz);
            final ByteBuf buf = Unpooled.wrappedBuffer(bytes);
            final T t = processor.process(buf);
            for (int i = 0; i < pools.length; i++) {
                pools[i].execute(() -> {
                    testDeParse(t, num, count);
                });
            }
        }
        final ScheduledExecutorService monitor = Executors.newSingleThreadScheduledExecutor();
        monitor.scheduleAtFixedRate(() -> {
            long sum = count.sumThenReset() / 3;
            logger.info("{} threadNum:{} num:{} totalSpeed/s:{} perThreadSpeed/s:{}", parse ? "parse" : "deParse", threadNum, num, sum, sum / threadNum);
        }, 3, 3, TimeUnit.SECONDS);

        try {
            for (ExecutorService pool : pools) {
                pool.shutdown();
            }
            for (ExecutorService pool : pools) {
                while (!pool.awaitTermination(1, TimeUnit.HOURS)) {

                }
            }
            monitor.shutdown();
            while (!monitor.awaitTermination(1, TimeUnit.HOURS)) {

            }
        } catch (InterruptedException e) {
            logger.error("interrupted", e);
        }
    }

    public static <T> void testParse(byte[] bytes, Class<T> clazz, int num, LongAdder count) {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        Processor<T> processor = Parser.getProcessor(clazz);
        for (int i = 1; i <= num; i++) {
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            final T t = processor.process(byteBuf);
            count.increment();
        }
    }

    public static <T> void testDeParse(T obj, int num, LongAdder count) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        Processor<T> processor = Parser.getProcessor((Class<T>) obj.getClass());
        for (int i = 1; i <= num; i++) {
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            processor.deProcess(byteBuf, obj);
            count.increment();
        }
    }

}
