package com.bcd.http;

import com.bcd.share.exception.BaseRuntimeException;
import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.impl.gb32960.data.Packet;
import com.bcd.share.support_parser.impl.gb32960.data.VehicleCommonData;
import com.bcd.share.support_parser.impl.gb32960.data.VehicleRunData;
import com.bcd.share.support_parser.processor.Processor;
import com.bcd.share.util.JsonUtil;
import io.jooby.WebSocket;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.*;

public class WsSession {
    static Logger logger = LoggerFactory.getLogger(WsSession.class);

    private final static String sample = "232302FE4C534A4533363039364D53313430343935010141170608100A10010103010040000003520F2827811C012E2000000002010101594FDB4E2F4A0F3227100500073944E501DD620A0601090E1B01370E14010145010444070300021387000000000801010F282781006C00016C0E180E190E1A0E190E190E180E180E1A0E1B0E180E190E1A0E180E180E190E1A0E1A0E190E180E1A0E180E1A0E1A0E180E170E190E170E190E170E190E1B0E190E190E190E180E180E170E170E180E170E170E170E190E170E180E170E190E170E170E170E180E180E190E190E140E180E180E170E170E150E160E160E180E190E170E180E170E180E170E180E170E160E190E150E180E160E180E170E160E160E170E150E170E170E140E170E160E160E170E170E170E170E160E170E160E170E140E170E170E160E160E170E170E170E160E160E160E16090101000C454545444544444445444544F5";
    public final static ConcurrentHashMap<WebSocket, WsSession> ws_session = new ConcurrentHashMap<>();
    static ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

    public final String vin;
    public final WebSocket ws;
    public Channel channel;
    public Packet packet;
    public ScheduledFuture<?> scheduledFuture;

    static NioEventLoopGroup tcp_workerGroup = new NioEventLoopGroup();

    public WsSession(String vin, WebSocket ws) {
        this.vin = vin;
        this.ws = ws;
        ws_session.put(ws, this);
        ws_onConnect(vin);
    }

    public static WsSession getSession(WebSocket ws) {
        return ws_session.get(ws);
    }

    public synchronized void ws_onConnect(String vin) {
        byte[] bytes = ByteBufUtil.decodeHexDump(sample);
        packet = HttpServer.processor.process(Unpooled.wrappedBuffer(bytes));
        packet.vin = vin;
        ws_send(new WsOutMsg(101, JsonUtil.toJson(((VehicleRunData) packet.data).vehicleCommonData), true));
    }

    public synchronized void ws_onClose() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        if (channel != null) {
            channel.close();
        }
        ws_session.remove(ws);
    }

    public synchronized void ws_handleMsg(WsInMsg inMsg) {
        switch (inMsg.flag()) {
            case 1 -> {
                String[] split = inMsg.data().split(":");
                try {
                    tcp_connect(split[0], Integer.parseInt(split[1]));
                    tcp_startSendRunData();
                    ws_send(new WsOutMsg(1, null, true));

                } catch (Exception ex) {
                    logger.error("connect tcp address[{}] error", inMsg.data(), ex);
                    ws_send(new WsOutMsg(1, null, false));
                }
            }
            case 2 -> {
                try {
                    ((VehicleRunData) (packet.data)).vehicleCommonData = JsonUtil.GLOBAL_OBJECT_MAPPER.readValue(inMsg.data(), VehicleCommonData.class);
                    ws_send(new WsOutMsg(2, null, true));
                } catch (IOException ex) {
                    logger.error("error", ex);
                    ws_send(new WsOutMsg(2, null, false));
                }
            }
        }
    }

    public synchronized void tcp_connect(String host, int port) {
        final WsSession wsSession = this;
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(tcp_workerGroup);
        bootstrap.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new TcpClientHandler(wsSession));
            }
        });
        try {
            channel = bootstrap.connect(host, port).sync().channel();
        } catch (InterruptedException e) {
            throw BaseRuntimeException.getException(e);
        }
    }

    public synchronized void tcp_onDisConnect() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        scheduledFuture = null;
        channel = null;
        ws_send(new WsOutMsg(104, null, true));
    }

    public synchronized void tcp_onMsg(ByteBuf byteBuf) {
        ws_send(new WsOutMsg(103, ByteBufUtil.hexDump(byteBuf), true));
    }

    public synchronized void ws_send(WsOutMsg outMsg) {
        if (ws.isOpen()) {
            ws.send(JsonUtil.toJson(outMsg));
        }
    }

    public synchronized void tcp_sendRunData() {
        ByteBuf buffer = Unpooled.buffer();
        ((VehicleRunData) packet.data).collectTime = new Date();
        HttpServer.processor.deProcess(buffer, packet);
        String hex = ByteBufUtil.hexDump(buffer);
        try {
            channel.writeAndFlush(buffer).sync();
        } catch (InterruptedException e) {
            throw BaseRuntimeException.getException(e);
        }
        ws_send(new WsOutMsg(102, hex, true));
    }

    public synchronized void tcp_startSendRunData() {
        scheduledFuture = pool.scheduleAtFixedRate(this::tcp_sendRunData, 1, 10, TimeUnit.SECONDS);
    }


}
