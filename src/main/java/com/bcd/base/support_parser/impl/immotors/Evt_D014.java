package com.bcd.base.support_parser.impl.immotors;


import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D014 extends Evt_4_x {
    @F_num(type = NumType.uint56)
    public long DTCInfomationICM;
    @F_num(type = NumType.uint56)
    public long DTCInfomationCARLog;
    @F_num(type = NumType.uint56)
    public long DTCInfomationIMATE;
    @F_num(type = NumType.uint56)
    public long DTCInfomationAMP;
    @F_num(type = NumType.uint56)
    public long DTCInfomationPGM;
}
