package com.bcd.base.support_parser.impl.immotors.ep33.data;


import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_string;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D01B extends Evt_4_x {
    @F_num(type = NumType.uint8)
    public short WANStatus;
    @F_num(type = NumType.uint8)
    public short ChannelType1;
    @F_num(type = NumType.uint8)
    public short ChannelStates1;
    @F_string(len = 18)
    public String IPAddress1;
    @F_num(type = NumType.uint8)
    public short ChannelType2;
    @F_num(type = NumType.uint8)
    public short ChannelStates2;
    @F_string(len = 18)
    public String IPAddress2;
    @F_num(type = NumType.uint8)
    public short ChannelType3;
    @F_num(type = NumType.uint8)
    public short ChannelStates3;
    @F_string(len = 18)
    public String IPAddress3;
    @F_num(type = NumType.uint8)
    public short ChannelType4;
    @F_num(type = NumType.uint8)
    public short ChannelStates4;
}
