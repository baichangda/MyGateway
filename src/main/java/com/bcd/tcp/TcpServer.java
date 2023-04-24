package com.bcd.tcp;

import com.bcd.properties.GatewayProp;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Component
public class TcpServer implements CommandLineRunner {

    @Autowired
    GatewayProp gatewayProp;

    final Logger logger = LoggerFactory.getLogger(TcpServer.class);

    @Autowired
    Handler_dispatch handler_dispatch;

    public void run(String... args) throws Exception {
        final EventLoopGroup boosGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(
                    new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ch.pipeline().addLast(new IdleStateHandler(0L, 0L, 30L, TimeUnit.SECONDS));
                            ch.pipeline().addLast(handler_dispatch);
                        }
                    }
            );
            final ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(gatewayProp.tcp.port)).sync();
            logger.info("server listen tcp port[{}]", gatewayProp.tcp.port);
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("run error", e);
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
