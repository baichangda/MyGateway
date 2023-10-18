package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.F_num_array;
import com.bcd.share.support_parser.anno.NumType;

public class TerminalRsa implements PacketBody{
    //rsa公钥{e,n}中的e
    @F_num(type = NumType.uint32)
    public long e;
    //rsa公钥{e,n}中的n
    @F_num_array(singleType = NumType.uint8, len = 128)
    public byte[] n;
}
