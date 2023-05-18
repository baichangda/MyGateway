package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_0800 extends Evt_2_6 {
    @F_integer(bit = 2)
    public byte SysPwrMd;
    @F_integer(bit = 1)
    public byte SysPwrMdV;
    @F_integer(bit = 1)
    public byte SysVolV;
    @F_integer(bit = 4)
    public byte TrShftLvrPos;
    @F_integer(bit = 8)
    public short SysVol;

    @F_skip(len = 3)
    public int skip;
    @F_integer(bit = 1)
    public byte TrShftLvrPosV;
}
