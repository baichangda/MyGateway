package com.bcd.tcp;

import com.bcd.tcp.gb32960.Handler_gb32960;
import com.bcd.tcp.gb32960.Save_gb32960;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@io.netty.channel.ChannelHandler.Sharable
public class Handler_dispatch extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(Handler_dispatch.class);

    @Autowired
    Save_gb32960 save_gb32960;

    @Autowired
    SessionClusterManager sessionClusterManager;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        //至少需要两个字节来判断
        if (in.readableBytes() >= 4) {
            final byte b0 = in.getByte(0);
            final byte b1 = in.getByte(1);
            final byte b2 = in.getByte(2);
            final byte b3 = in.getByte(3);
            if (b0 == 0x23 && b1 == 0x23) {
                ctx.pipeline().addLast(new LengthFieldBasedFrameDecoder(10 * 1024, 22, 2, 1, 0));
                ctx.pipeline().addLast(new Handler_gb32960(save_gb32960, sessionClusterManager));
                ctx.pipeline().remove(this);
            } else {
                logger.info("receive header[{},{},{},{}]、close channel", b0, b1, b2, b3);
                // 主动断开
                ctx.channel().close();
            }
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught", cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            logger.info("[{}] trigger", ((IdleStateEvent) evt).state());
            ctx.channel().close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
