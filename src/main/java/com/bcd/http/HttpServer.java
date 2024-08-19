package com.bcd.http;

import com.bcd.base.util.JsonUtil;
import io.helidon.cors.CrossOriginConfig;
import io.helidon.http.media.jackson.JacksonSupport;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.WebServerConfig;
import io.helidon.webserver.cors.CorsFeature;
import io.helidon.webserver.cors.CorsSupport;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.staticcontent.StaticContentService;
import io.helidon.webserver.websocket.WsRouting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

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
                        //自动发现引入的组件、即引入的组件jar不用手动代码调用
                        .featuresDiscoverServices(true)
                        .protocolsDiscoverServices(true)
                        //手动引入、优先级更高、会覆盖自动发现的组件
                        .mediaContext(e -> e.addMediaSupport(JacksonSupport.create(JsonUtil.OBJECT_MAPPER)))
                        .addFeature(CorsFeature.create(e->e.enabled(true)));
                WsRouting.Builder wsRoutingBuilder = WsRouting.builder();
                for (HttpServerBuilder handler : handlers) {
                    handler.build(httpRoutingBuilder, wsRoutingBuilder);
                }
                builder.addRouting(httpRoutingBuilder);
                builder.addRouting(wsRoutingBuilder);
                builder.port(httpProp.port).build().start();
            }
        });
    }
}
