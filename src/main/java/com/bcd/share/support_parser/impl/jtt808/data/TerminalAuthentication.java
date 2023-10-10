package com.bcd.share.support_parser.impl.jtt808.data;


import com.bcd.share.support_parser.anno.*;

public class TerminalAuthentication implements PacketBody {
    //鉴权码长度
    @F_num(type = NumType.uint8, var = 'n')
    public short codeLen;
    //鉴权码内容
    @F_string(lenExpr = "n", charset = "GBK", appendMode = StringAppendMode.noAppend)
    public String code;
    //终端imei
    @F_num_array(singleType = NumType.uint8, len = 15)
    public byte[] imei;
    //软件版本号
    @F_num_array(singleType = NumType.uint8, len = 20)
    public byte[] version;

}
