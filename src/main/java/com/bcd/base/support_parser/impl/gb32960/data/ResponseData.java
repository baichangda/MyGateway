package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.anno.C_impl;
import com.bcd.base.support_parser.anno.F_num_array;
import com.bcd.base.support_parser.anno.NumType;

@C_impl(value = Integer.MAX_VALUE)
public class ResponseData implements PacketData {
    @F_num_array(singleType = NumType.uint8,lenExpr = "@a")
    public byte[] content;
}
