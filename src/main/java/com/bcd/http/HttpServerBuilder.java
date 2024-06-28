package com.bcd.http;

import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.websocket.WsRouting;

public interface HttpServerBuilder {
    void build(HttpRouting.Builder httpRoutingBuilder, WsRouting.Builder wsRoutingBuilder);
}
