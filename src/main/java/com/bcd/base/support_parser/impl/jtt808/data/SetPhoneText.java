package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.anno.F_bean_list;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

public class SetPhoneText implements PacketBody {
    //设置类型
    @F_num(type = NumType.uint8)
    public byte type;
    //联系人总数
    @F_num(type = NumType.uint8, var = 'n')
    public short total;
    //联系人项
    @F_bean_list(listLenExpr = "n")
    public PhoneContactPerson[] persons;

}
