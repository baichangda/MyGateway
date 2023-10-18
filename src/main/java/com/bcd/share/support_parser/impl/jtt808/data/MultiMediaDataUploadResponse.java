package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;
import io.netty.buffer.ByteBuf;

public class MultiMediaDataUploadResponse implements PacketBody {
    //多媒体数据id
    @F_num(type = NumType.uint32)
    public long id;
    //重传包总数
    @F_num(type = NumType.uint8)
    public short num;
    //重传包id列表
    public byte[] data;

    public static MultiMediaDataUploadResponse read(ByteBuf data, int len) {
        MultiMediaDataUploadResponse multiMediaDataUploadResponse = new MultiMediaDataUploadResponse();
        multiMediaDataUploadResponse.id = data.readUnsignedInt();
        multiMediaDataUploadResponse.num = data.readUnsignedByte();
        byte[] bytes = new byte[len - 5];
        data.readBytes(bytes);
        multiMediaDataUploadResponse.data = bytes;
        return multiMediaDataUploadResponse;
    }
}
