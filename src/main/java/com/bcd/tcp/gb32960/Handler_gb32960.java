package com.bcd.tcp.gb32960;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.impl.gb32960.data.Packet;
import com.bcd.tcp.SessionClusterManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Handler_gb32960 extends ChannelInboundHandlerAdapter {

    static Logger logger= LoggerFactory.getLogger(Handler_gb32960.class);

    Session_gb32960 session;

    public final ConcurrentLinkedQueue<Packet> queue;

    public final SessionClusterManager sessionClusterManager;

    public Handler_gb32960(Save_gb32960 save_gb32960, SessionClusterManager sessionClusterManager) {
        this.queue = save_gb32960.queue;
        this.sessionClusterManager = sessionClusterManager;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //解析
        final Packet packet = Parser.parse(Packet.class, (ByteBuf) msg, null);
        if (session == null) {
            //构造会话
            session = new Session_gb32960(packet.vin, ctx.channel());
            //发送会话通知到其他集群、踢掉无用的session
            sessionClusterManager.send(session);
        }
        //添加到保存队列
        queue.add(packet);
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //关闭
        if(session!=null){
            session.close();
        }
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught",cause);
        //关闭
        ctx.close();
    }
}
