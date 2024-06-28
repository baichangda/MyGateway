package com.bcd.base.support_parser.impl.gb32960.data;

import com.bcd.base.support_parser.anno.C_impl;

@C_impl(value = Integer.MAX_VALUE)
public class ResponseData implements PacketData {
    public byte[] content;
}
