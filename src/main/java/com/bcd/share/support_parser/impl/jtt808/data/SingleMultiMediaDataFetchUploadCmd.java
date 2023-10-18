package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;


public class SingleMultiMediaDataFetchUploadCmd implements PacketBody{
    //多媒体id
    @F_num(type = NumType.uint32)
    public long id;
    //删除标志
    @F_num(type = NumType.uint8)
    public byte flag;
}
