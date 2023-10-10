package com.bcd.tcp.gb32960;

import com.bcd.tcp.Session;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class Session_gb32960 extends Session {

    static {
        Session.sessionMaps[Type.gb32960.type] = new ConcurrentHashMap<>();
    }

    public Session_gb32960(String id, Channel channel) {
        super(Type.gb32960.type, id, channel);
    }

    public static ConcurrentHashMap<String, Session> getSessionMap() {
        return Session.sessionMaps[Type.gb32960.type];
    }
}
