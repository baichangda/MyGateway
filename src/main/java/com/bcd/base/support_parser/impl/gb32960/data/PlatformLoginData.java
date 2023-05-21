package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.anno.DateMode;
import com.bcd.base.support_parser.anno.F_date;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_string;

import java.util.Date;

public class PlatformLoginData implements PacketData {
    //平台登入时间
    @F_date(mode = DateMode.Bytes_yyMMddHHmmss, baseYear = 2000)
    public Date collectTime;

    //登入流水号
    @F_num(len = 2)
    public int sn;

    //平台用户名
    @F_string(len = 12)
    public String username;

    //平台密码
    @F_string(len = 20)
    public String password;

    //加密规则
    @F_num(len = 1)
    public short encode;
}
