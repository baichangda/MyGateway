package com.bcd.tcp;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class Session {
    public final static ConcurrentHashMap<String, Session>[] sessionMaps = new ConcurrentHashMap[2];
    public final int type;
    public final String id;
    public final long createTs;
    public final Channel channel;
    public boolean closed;

    public Session(int type, String id, Channel channel) {
        this.type = type;
        this.id = id;
        this.channel = channel;
        this.createTs = System.currentTimeMillis();
        sessionMaps[type].put(id, this);
    }

    public static Session getSession(int type, String id) {
        return sessionMaps[type].get(id);
    }

    public void close() {
        channel.eventLoop().execute(() -> {
            if (!closed) {
                //移除会话
                sessionMaps[type].remove(id);
                //关闭会话
                channel.close();
                closed = true;
            }
        });
    }

    public enum Type {
        gb32960(0),
        jt808(1),
        ;
        public final int type;

        Type(int type) {
            this.type = type;
        }
    }
}
