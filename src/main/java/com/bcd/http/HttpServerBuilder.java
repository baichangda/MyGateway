package com.bcd.http;

import io.undertow.server.handlers.PathHandler;

public interface HttpServerBuilder {
    void build(PathHandler pathHandler);
}
