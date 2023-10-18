package com.bcd.share.support_parser.impl.jtt808.data;


import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public class Position implements PacketBody {
    //位置基础数据
    public PositionBase base;
    //位置附加数据
    public PositionExt[] exts;
    public static Position read(ByteBuf data, int len) {
        Position position = new Position();
        position.base = PositionBase.read(data);
        int byteLen = len - 28;
        int readerIndex = data.readerIndex();
        ArrayList<PositionExt> list = new ArrayList<>();
        while (byteLen > data.readerIndex() - readerIndex) {
            list.add(PositionExt.read(data));
        }
        position.exts = list.toArray(new PositionExt[0]);
        return position;
    }
}
