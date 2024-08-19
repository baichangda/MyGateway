package com.bcd.http;

import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.websocket.WsRouting;

public interface HttpServerBuilder {
    /**
     * 实现注册http服务和ws服务
     * @param httpRoutingBuilder http构建器
     * @param wsRoutingBuilder ws构建器
     */
    void build(HttpRouting.Builder httpRoutingBuilder, WsRouting.Builder wsRoutingBuilder);
}
