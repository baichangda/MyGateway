package com.bcd.tcp;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class Session {
    public final int type;
    public final String id;
    public final long createTs;
    public final Channel channel;
    public boolean closed;

    public final static ConcurrentHashMap<String, Session>[] sessions = new ConcurrentHashMap[2];

    public Session(int type, String id, Channel channel) {
        this.type = type;
        this.id = id;
        this.channel = channel;
        this.createTs = System.currentTimeMillis();
        sessions[type].put(id, this);
    }

    public static Session getSession(int type, String id) {
        return sessions[type].get(id);
    }

    public void close() {
        channel.eventLoop().execute(() -> {
            if (!closed) {
                //移除会话
                sessions[type].remove(id);
                //关闭会话
                channel.close();
                closed = true;
            }
        });
    }

    public enum Type {
        gb32960(0),
        icd(1),
        ;
        public final int type;

        Type(int type) {
            this.type = type;
        }
    }
}
