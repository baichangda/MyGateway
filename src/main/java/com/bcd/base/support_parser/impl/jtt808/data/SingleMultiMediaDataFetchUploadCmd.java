package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;


public class SingleMultiMediaDataFetchUploadCmd implements PacketBody{
    //多媒体id
    @F_num(type = NumType.uint32)
    public long id;
    //删除标志
    @F_num(type = NumType.uint8)
    public byte flag;
}
