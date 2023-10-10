package com.bcd.root.support_parser.impl.jtt808.data;

import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.F_string;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.anno.StringAppendMode;

public class PhoneContactPerson {
    //标志
    @F_num(type = NumType.uint8)
    public byte flag;
    //号码长度
    @F_num(type = NumType.uint8, var = 'a')
    public short phoneNumberLen;
    //电话号码
    @F_string(lenExpr = "a", charset = "GBK", appendMode = StringAppendMode.noAppend)
    public String phoneNumber;
    //联系人长度
    @F_num(type = NumType.uint8, var = 'b')
    public short concatPersonLen;
    //联系人
    @F_string(lenExpr = "b", charset = "GBK", appendMode = StringAppendMode.noAppend)
    public String concatPerson;
}
