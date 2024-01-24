package com.bcd.tcp.gb32960;

import com.bcd.tcp.SessionClusterManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler_gb32960 extends ChannelInboundHandlerAdapter {

    static Logger logger = LoggerFactory.getLogger(Handler_gb32960.class);
    public final Save_gb32960 save;
    public final SessionClusterManager sessionClusterManager;
    Session_gb32960 session;

    public Handler_gb32960(Save_gb32960 save, SessionClusterManager sessionClusterManager) {
        this.save = save;
        this.sessionClusterManager = sessionClusterManager;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Monitor_gb32960.receiveNum.increment();
        //读取数据
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        //轻量解析
        SaveData saveData = SaveData.readSaveData(bytes);

        if (session == null) {
            //构造会话
            session = new Session_gb32960(saveData.jsonData.vin, ctx.channel());
            //发送会话通知到其他集群、踢掉无用的session
            sessionClusterManager.send(session);
        }

        //添加到保存队列
        save.put(saveData);
        Monitor_gb32960.queueNum.increment();
        //响应数据
        ctx.writeAndFlush(Unpooled.wrappedBuffer(response_succeed(bytes)));
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //关闭
        if (session != null) {
            session.close();
        }
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught", cause);
        //关闭
        ctx.close();
    }

    private byte[] response_succeed(byte[] src) {
        byte[] dest = new byte[25];
        System.arraycopy(src, 0, dest, 0, 3);
        dest[3] = 0x01;
        System.arraycopy(src, 4, dest, 4, 18);
        byte xor = 0;
        for (int i = 0; i < 24; i++) {
            xor ^= src[i];
        }
        dest[24] = xor;
        return dest;
    }
}
