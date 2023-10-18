package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.support_parser.anno.F_bean;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;
import io.netty.buffer.ByteBuf;

public class MultiMediaDataUploadRequest implements PacketBody {
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
    //位置信息汇报
    @F_bean
    public PositionBase position;

    //多媒体数据包
    public byte[] data;

    public static MultiMediaDataUploadRequest read(ByteBuf data, int len) {
        MultiMediaDataUploadRequest multiMediaDataUploadRequest = new MultiMediaDataUploadRequest();
        multiMediaDataUploadRequest.id = data.readUnsignedInt();
        multiMediaDataUploadRequest.type = data.readByte();
        multiMediaDataUploadRequest.code = data.readByte();
        multiMediaDataUploadRequest.eventCode = data.readByte();
        multiMediaDataUploadRequest.channelId = data.readUnsignedByte();
        multiMediaDataUploadRequest.position = PositionBase.read(data);
        byte[] bytes = new byte[len - 36];
        data.readBytes(bytes);
        multiMediaDataUploadRequest.data = bytes;
        return multiMediaDataUploadRequest;
    }
}
