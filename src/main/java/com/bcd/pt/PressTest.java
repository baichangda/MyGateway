package com.bcd.pt;

import com.bcd.http.TcpClientHandler;
import com.bcd.share.exception.BaseRuntimeException;
import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.impl.gb32960.data.Packet;
import com.bcd.share.support_parser.impl.gb32960.data.VehicleRunData;
import com.bcd.share.support_parser.processor.Processor;
import com.google.common.base.Strings;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

//@Component
public class PressTest implements CommandLineRunner {

    static Logger logger = LoggerFactory.getLogger(PressTest.class);


    final static String sample = "232302FE4C534A4533363039364D53313430343935010141170608100A10010103010040000003520F2827811C012E2000000002010101594FDB4E2F4A0F3227100500073944E501DD620A0601090E1B01370E14010145010444070300021387000000000801010F282781006C00016C0E180E190E1A0E190E190E180E180E1A0E1B0E180E190E1A0E180E180E190E1A0E1A0E190E180E1A0E180E1A0E1A0E180E170E190E170E190E170E190E1B0E190E190E190E180E180E170E170E180E170E170E170E190E170E180E170E190E170E170E170E180E180E190E190E140E180E180E170E170E150E160E160E180E190E170E180E170E180E170E180E170E160E190E150E180E160E180E170E160E160E170E150E170E170E140E170E160E160E170E170E170E170E160E170E160E170E140E170E170E160E160E170E170E170E160E160E160E16090101000C454545444544444445444544F5";
    final static Processor<Packet> processor = Parser.getProcessor(Packet.class);

    @Autowired
    PtProp ptProp;

    AtomicInteger clientNum = new AtomicInteger();
    AtomicInteger sendNum = new AtomicInteger();

    static NioEventLoopGroup tcp_workerGroup = new NioEventLoopGroup();

    @Override
    public void run(String... args) throws Exception {
        Thread.startVirtualThread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                try (ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()) {
                    scheduledExecutorService.scheduleAtFixedRate(() -> {
                        logger.info("client[{}] sendSpeed[{}]/s", clientNum.get(), sendNum.getAndSet(0) / 3);
                    }, 1, 3, TimeUnit.SECONDS);
                    AtomicBoolean running = new AtomicBoolean(true);
                    String[] vins = getVins();
                    int period = ptProp.period;
                    if (vins.length < period) {
                        for (String vin : vins) {
                            startClient(vin, running);
                        }
                    } else {
                        int batchNum = ptProp.num / period;
                        for (int i = 0; i < period; i++) {
                            if (i == period - 1) {
                                for (int j = i * batchNum; j < vins.length; j++) {
                                    startClient(vins[j], running);
                                }
                            } else {
                                for (int j = i * batchNum; j < (i + 1) * batchNum; j++) {
                                    startClient(vins[j], running);
                                }
                            }
                            TimeUnit.SECONDS.sleep(1);
                        }
                    }
                    while (running.get()) {
                        TimeUnit.SECONDS.sleep(3);
                    }
                }
            } catch (Exception ex) {
                throw BaseRuntimeException.getException(ex);
            }
        });
    }

    private void doBeforeSend(Packet packet, long sendTs) {
        ((VehicleRunData) packet.data).collectTime = new Date(sendTs);
    }

    public void startClient(String vin, AtomicBoolean running) {
        if (!running.get()) {
            return;
        }
        int period = ptProp.period;
        String server = ptProp.server;
        String[] split = server.split(":");
        Thread.startVirtualThread(() -> {
            if (!running.get()) {
                return;
            }
            byte[] bytes = ByteBufUtil.decodeHexDump(sample);
            Packet packet = processor.process(Unpooled.wrappedBuffer(bytes));
            packet.vin = vin;
            boolean add = false;
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(tcp_workerGroup);
                bootstrap.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(10 * 1024, 22, 2, 1, 0));
                    }
                });
                Channel channel = bootstrap.connect(split[0], Integer.parseInt(split[1])).sync().channel();
                clientNum.incrementAndGet();
                add = true;
                long sendTs = System.currentTimeMillis();
                while (running.get()) {
                    long waitMills = sendTs - System.currentTimeMillis();
                    if (waitMills > 0) {
                        TimeUnit.MILLISECONDS.sleep(waitMills);
                    }
                    if (!running.get()) {
                        return;
                    }
                    doBeforeSend(packet, sendTs);
                    ByteBuf buffer = Unpooled.buffer();
                    processor.deProcess(buffer, packet);
                    channel.writeAndFlush(buffer);
                    sendNum.incrementAndGet();
                    sendTs += period * 1000L;
                }
            } catch (Exception e) {
                logger.error("error", e);
                running.set(false);
            }
            if (add) {
                clientNum.decrementAndGet();
            }
        });
    }

    public String[] getVins() {
        String vinPrefix = "TEST000000";
        int num = ptProp.num;
        int startIndex = ptProp.startIndex;
        String[] vins = new String[num];
        for (int i = startIndex; i < startIndex + num; i++) {
            vins[i - startIndex] = vinPrefix + Strings.padStart(i + "", 7, '0');
        }
        return vins;
    }
}
