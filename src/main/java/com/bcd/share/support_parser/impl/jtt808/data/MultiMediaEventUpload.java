package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;

public class MultiMediaEventUpload implements PacketBody {
    //多媒体数据id
    @F_num(type = NumType.uint32)
    public long id;
    //多媒体类型
    @F_num(type = NumType.uint8)
    public byte type;
    //多媒体格式编码
    @F_num(type = NumType.uint8)
    public byte code;
    //事件项编码
    @F_num(type = NumType.uint8)
    public byte eventCode;
    //通道id
    @F_num(type = NumType.uint8)
    public short channelId;
}
