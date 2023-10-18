package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.support_parser.anno.DateMode;
import com.bcd.share.support_parser.anno.F_date;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;
import io.netty.buffer.ByteBuf;

import java.util.Date;

public class StorageMultiMediaDataFetchResponse implements PacketBody{
    //应答流水号
    public int sn;
    //多媒体数据总项数
    public int num;
    //多媒体id
    public long id;
    //多媒体类型
    public byte type;
    //通道id
    public short channelId;
    //事件项编码
    public byte code;
    //位置信息汇报
    public Position position;

    public static StorageMultiMediaDataFetchResponse read(ByteBuf data,int len){
        StorageMultiMediaDataFetchResponse storageMultiMediaDataFetchResponse=new StorageMultiMediaDataFetchResponse();
        storageMultiMediaDataFetchResponse.sn=data.readUnsignedShort();
        storageMultiMediaDataFetchResponse.num=data.readUnsignedShort();
        storageMultiMediaDataFetchResponse.id=data.readUnsignedInt();
        storageMultiMediaDataFetchResponse.type=data.readByte();
        storageMultiMediaDataFetchResponse.channelId=data.readUnsignedByte();
        storageMultiMediaDataFetchResponse.code=data.readByte();
        storageMultiMediaDataFetchResponse.position=Position.read(data,len-11);
        return storageMultiMediaDataFetchResponse;
    }
}

