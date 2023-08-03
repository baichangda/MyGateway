package com.bcd.tcp.icd;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.impl.icd.data.Msg;
import com.bcd.base.support_parser.processor.Processor;
import com.bcd.tcp.SessionClusterManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Handler_icd extends ChannelInboundHandlerAdapter {

    static Logger logger = LoggerFactory.getLogger(Handler_icd.class);

    Session_icd session;

    public final ConcurrentLinkedQueue<Msg> queue;

    public final SessionClusterManager sessionClusterManager;

    final static Processor<Msg> processor = Parser.getProcessor(Msg.class);

    public Handler_icd(Save_icd save_icd, SessionClusterManager sessionClusterManager) {
        this.queue = save_icd.queue;
        this.sessionClusterManager = sessionClusterManager;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        final ByteBuf byteBuf = (ByteBuf) obj;
        //校验
        if (!Msg.check_sum(byteBuf)) {
            logger.warn("check_sum failed hex[{}]", ByteBufUtil.hexDump(byteBuf));
            return;
        }
        //解析
        try {
            final Msg msg = processor.process(byteBuf, null);
            if (session == null) {
                //构造会话
                session = new Session_icd(String.valueOf(msg.msg_header.device_sn), ctx.channel());
                //发送会话通知到其他集群、踢掉无用的session
                sessionClusterManager.send(session);
            }
            //添加到保存队列
            queue.add(msg);
            super.channelRead(ctx, msg);
        } catch (Exception ex) {
            logger.error("parse error hex[{}]", ByteBufUtil.hexDump(byteBuf), ex);
        }
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


}
