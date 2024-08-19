package com.bcd.http.gb32960;

import cn.bcd.parser.protocol.gb32960.data.Packet;
import com.bcd.base.util.JsonUtil;
import com.bcd.http.HttpServerBuilder;
import com.bcd.http.WsInMsg;
import com.bcd.http.WsSession;
import io.helidon.http.HeaderNames;
import io.helidon.http.Headers;
import io.helidon.http.HttpMediaType;
import io.helidon.http.HttpPrologue;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.staticcontent.StaticContentService;
import io.helidon.webserver.websocket.WsRouting;
import io.helidon.websocket.WsListener;
import io.helidon.websocket.WsUpgradeException;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@ConditionalOnProperty("http.port")
@Component
public class HttpServerBuilder_gb32960 implements HttpServerBuilder {
    static Logger logger = LoggerFactory.getLogger(HttpServerBuilder_gb32960.class);

    public void build(HttpRouting.Builder httpRoutingBuilder, WsRouting.Builder wsRoutingBuilder) {
        Thread.startVirtualThread(() -> {
//            StaticContentService.FileSystemBuilder builder = StaticContentService.builder(Paths.get("src/main/resources/http/gb32960");
            StaticContentService.ClassPathBuilder builder = StaticContentService.builder("http/gb32960");
            httpRoutingBuilder
                    .register("/gb32960", builder.welcomeFileName("index.html").contentType(".html", HttpMediaType.create("text/html;charset=utf-8")))
                    .post("/parse/gb32960", (req, rep) -> {
                        String hex = req.content().as(String.class);
                        rep.header(HeaderNames.CONTENT_TYPE, "application/json;charset=utf-8");
                        try {
                            byte[] bytes = ByteBufUtil.decodeHexDump(hex);
                            try {
                                Packet packet = Packet.read(Unpooled.wrappedBuffer(bytes));
                                String json = JsonUtil.toJson(packet);
                                rep.send(JsonUtil.toJson(Map.of("data", json, "succeed", true)));
                            } catch (Exception ex) {
                                logger.error("parse protocol error:\n{}", hex, ex);
                                rep.send(JsonUtil.toJson(Map.of("msg", "解析失败、报文不符合格式", "succeed", false)));
                            }
                        } catch (Exception ex) {
                            logger.error("parse hex error:\n{}", hex, ex);
                            rep.send(JsonUtil.toJson(Map.of("msg", "解析失败、报文不是16进制格式", "succeed", false)));
                        }
                    });
            wsRoutingBuilder.endpoint("/ws/gb32960", () -> new WsListener() {
                private String vin;
                private WsSession<Packet> wsSession;
                private StringBuilder sb = new StringBuilder();


                @Override
                public Optional<Headers> onHttpUpgrade(HttpPrologue prologue, Headers headers) throws WsUpgradeException {
                    this.vin = prologue.query().get("vin");
                    return WsListener.super.onHttpUpgrade(prologue, headers);
                }

                @Override
                public void onOpen(io.helidon.websocket.WsSession session) {
                    wsSession = new WsSession_gb32960(session, vin);
                    WsListener.super.onOpen(session);
                }

                @Override
                public void onMessage(io.helidon.websocket.WsSession session, String text, boolean last) {
                    sb.append(text);
                    if (last) {
                        String data = sb.toString();
                        sb = new StringBuilder();
                        try {
                            WsInMsg wsInMsg = JsonUtil.OBJECT_MAPPER.readValue(data, WsInMsg.class);
                            wsSession.ws_onMsg(wsInMsg);
                        } catch (IOException ex) {
                            logger.error("receive ws msg parse json error:\n{}", data);
                        }
                    }
                    WsListener.super.onMessage(session, text, last);
                }

                @Override
                public void onClose(io.helidon.websocket.WsSession session, int status, String reason) {
                    wsSession.ws_onClose();
                    WsListener.super.onClose(session, status, reason);
                }

                @Override
                public void onError(io.helidon.websocket.WsSession session, Throwable t) {
                    wsSession.ws_onClose();
                    WsListener.super.onError(session, t);
                }
            });
        });

    }
}
