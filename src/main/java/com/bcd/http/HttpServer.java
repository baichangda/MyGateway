package com.bcd.http;

import com.bcd.base.exception.BaseException;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.resource.ResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@ConditionalOnProperty("http.port")
@Component
public class HttpServer implements CommandLineRunner {
    public final static HandlerWrapper encodinghandlerWrapper = new EncodingHandler.Builder().build(null);
    public final static HandlerWrapper accessLogHandler = new AccessLogHandler.Builder().build(Map.of("format", "common", "category", "bcd"));

    @Autowired
    public HttpProp httpProp;

    @Autowired
    List<HttpServerBuilder> handlers;

    @Override
    public void run(String... args) throws Exception {
        Thread.startVirtualThread(() -> {
            try (ResourceManager resourceManager = new ClassPathResourceManager(HttpServer.class.getClassLoader(), "http/common")) {
                ResourceHandler resourceHandler = new ResourceHandler(resourceManager);
                PathHandler pathHandler = Handlers.path()
                        .addPrefixPath("/common", resourceHandler);
                for (HttpServerBuilder handler : handlers) {
                    handler.build(pathHandler);
                }
                Undertow.builder().addHttpListener(httpProp.port, "0.0.0.0").setHandler(accessLogHandler.wrap(encodinghandlerWrapper.wrap(pathHandler))).build().start();
            } catch (IOException ex) {
                throw BaseException.get(ex);
            }
        });
    }
}
