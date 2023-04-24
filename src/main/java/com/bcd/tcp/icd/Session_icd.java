package com.bcd.tcp.icd;

import com.bcd.tcp.Session;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class Session_icd extends Session {

    static {
        Session.sessions[Type.icd.type] = new ConcurrentHashMap<>();
    }
    public Session_icd(String id, Channel channel) {
        super(Type.icd.type, id, channel);
    }
}
