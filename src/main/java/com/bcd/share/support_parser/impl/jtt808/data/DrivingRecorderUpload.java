package com.bcd.share.support_parser.impl.jtt808.data;

import io.netty.buffer.ByteBuf;

public class DrivingRecorderUpload implements PacketBody{
    //应答流水号
    public int sn;
    //命令字
    public byte flag;
    //数据块
    public byte[] content;

    public static DrivingRecorderUpload read(ByteBuf data,int len){
        DrivingRecorderUpload drivingRecorderUpload = new DrivingRecorderUpload();
        drivingRecorderUpload.sn = data.readUnsignedShort();
        drivingRecorderUpload.flag = data.readByte();
        byte[] content = new byte[len - 3];
        data.readBytes(content);
        drivingRecorderUpload.content = content;
        return drivingRecorderUpload;
    }
}
