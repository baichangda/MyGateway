package com.bcd.base.support_parser.impl.immotors;

import com.bcd.base.support_parser.anno.F_string;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00A extends Evt_4_x {
    @F_string(len = 17)
    public String VIN;
    @F_string(len = 16)
    public String IAMSN;
    @F_string(len = 20)
    public String EsimIccid;
    @F_string(len = 32)
    public String EsimID;
}
