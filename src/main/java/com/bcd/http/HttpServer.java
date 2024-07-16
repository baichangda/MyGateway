package com.bcd.http;

import com.bcd.base.support_parser.impl.gb32960.data.Packet;
import com.bcd.base.util.JsonUtil;
import com.bcd.http.gb32960.WsSession_gb32960;
import io.helidon.cors.CrossOriginConfig;
import io.helidon.http.Headers;
import io.helidon.http.HttpMediaType;
import io.helidon.http.HttpPrologue;
import io.helidon.http.encoding.deflate.DeflateEncoding;
import io.helidon.http.encoding.gzip.GzipEncoding;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.WebServerConfig;
import io.helidon.webserver.accesslog.AccessLogFeature;
import io.helidon.webserver.cors.CorsSupport;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.staticcontent.StaticContentService;
import io.helidon.webserver.websocket.WsRouting;
import io.helidon.websocket.WsListener;
import io.helidon.websocket.WsSession;
import io.helidon.websocket.WsUpgradeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.List;

@ConditionalOnProperty("http.port")
@Component
public class HttpServer implements CommandLineRunner {

    @Autowired
    public HttpProp httpProp;

    @Autowired
    List<HttpServerBuilder> handlers;

    @Override
    public void run(String... args) throws Exception {
        Thread.startVirtualThread(() -> {
            if (!handlers.isEmpty()) {
//                StaticContentService.FileSystemBuilder staticBuilder = StaticContentService.builder(Paths.get("src/main/resources/http/common"));
                StaticContentService.ClassPathBuilder staticBuilder = StaticContentService.builder("http/common");
                HttpRouting.Builder httpRoutingBuilder = HttpRouting.builder()
                        .register("/common", staticBuilder);
                WebServerConfig.Builder builder = WebServer.builder()
                        .addFeature(AccessLogFeature.builder().defaultLogFormat().build())
                        .contentEncoding(e ->
                                e.contentEncodingsDiscoverServices(false)
                                        .addContentEncoding(GzipEncoding.create())
                                        .addContentEncoding(DeflateEncoding.create())
                        );
                CorsSupport corsSupport = CorsSupport.builder().addCrossOrigin(
                        CrossOriginConfig
                                .builder()
                                .allowOrigins("*")
                                .allowMethods("GET","POST","PUT","OPTIONS","DELETE")
                                .maxAgeSeconds(0)
                                //表明哪些headers可以暴露给客户端使用
                                .exposeHeaders()
                                .enabled(true)
                                .build()).build();
                WsRouting.Builder wsRoutingBuilder = WsRouting.builder();
                for (HttpServerBuilder handler : handlers) {
                    handler.build(httpRoutingBuilder,wsRoutingBuilder);
                }
                builder.addRouting(httpRoutingBuilder.register(corsSupport));
                builder.addRouting(wsRoutingBuilder);
                builder.port(httpProp.port).build().start();
            }
        });
    }
}
