package com.bcd.http;

import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.impl.gb32960.data.Packet;
import com.bcd.share.support_parser.processor.Processor;
import com.bcd.share.util.DateZoneUtil;
import com.bcd.share.util.JsonUtil;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.jooby.*;
import io.jooby.handler.AccessLogHandler;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpHeaderNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@ConditionalOnProperty("http.port")
@Component
public class HttpServer implements CommandLineRunner {

    static Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static Processor<Packet> processor = Parser.getProcessor(Packet.class);


    @Override
    public void run(String... args) {
        Thread.startVirtualThread(() -> {
            Jooby.runApp(args, app -> {
                app.use(new AccessLogHandler().dateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(DateZoneUtil.ZONE_OFFSET)));
                ServerOptions serverOptions=new ServerOptions();
                serverOptions.setPort(9999);
                serverOptions.setCompressionLevel(ServerOptions.DEFAULT_COMPRESSION_LEVEL);
                app.setServerOptions(serverOptions);
                //静态文件
                app.get("/", ctx -> {
                    ctx.sendRedirect(StatusCode.MOVED_PERMANENTLY,"/gb32960/");
                    return ctx;
                });
//            app.assets("/gb32960/*", new ClassPathAssetSource(HttpServer.class.getClassLoader(), "http/gb32960"));
                app.assets("/gb32960/*", "src/main/resources/http/gb32960");
                //websocket
                app.ws("/ws/gb32960", (ctx, configurer) -> {
                    configurer.onConnect(ws -> {
                        String vin = ctx.query("vin").value();
                        new WsSession(vin, ws);
                    });
                    configurer.onMessage((ws, message) -> {
                        try {
                            WsInMsg wsInMsg = JsonUtil.GLOBAL_OBJECT_MAPPER.readValue(message.value(), WsInMsg.class);
                            WsSession session = WsSession.getSession(ws);
                            if (session != null) {
                                session.ws_handleMsg(wsInMsg);
                            }
                        } catch (IOException ex) {
                            logger.error("receive ws msg parse json error:\n{}", message.value());
                        }
                    });
                    configurer.onClose((ws, closeStatus) -> {
                        WsSession session = WsSession.getSession(ws);
                        session.ws_onClose();
                    });
                    configurer.onError((ws, cause) -> {
                        logger.error("ws onError", cause);
                        WsSession session = WsSession.getSession(ws);
                        session.ws_onClose();
                    });
                });
                //解析
                app.get("/parse/gb32960", ctx -> {
                    String hex = ctx.query("hex").value();
                    ctx.setResponseHeader(HttpHeaderNames.CONTENT_TYPE.toString(), "application/json;charset=utf-8");
                    String res;
                    try {
                        byte[] bytes = ByteBufUtil.decodeHexDump(hex);
                        try {
                            Packet packet = processor.process(Unpooled.wrappedBuffer(bytes));
                            String json = JsonUtil.toJson(packet);
                            res = JsonUtil.toJson(Map.of("data", json, "succeed", true));
                        } catch (Exception ex) {
                            logger.error("parse gb32960 error:\n{}", hex, ex);
                            res = JsonUtil.toJson(Map.of("msg", "解析失败、报文不符合32960格式", "succeed", false));
                        }
                        return res;
                    } catch (Exception ex) {
                        logger.error("parse hex error:\n{}", hex, ex);
                        return JsonUtil.toJson(Map.of("msg", "解析失败、报文不是16进制格式", "succeed", false));
                    }
                });
            });
        });
    }
}
