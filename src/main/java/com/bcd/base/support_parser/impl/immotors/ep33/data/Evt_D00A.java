package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_string;
import com.bcd.base.support_parser.anno.StringAppendMode;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00A extends Evt_4_x {
    @F_string(len = 17, appendMode = StringAppendMode.NoAppend)
    public String VIN;
    @F_string(len = 16, appendMode = StringAppendMode.NoAppend)
    public long IAMSN;
    @F_string(len = 20, appendMode = StringAppendMode.NoAppend)
    public long EsimIccid;
    @F_string(len = 32, appendMode = StringAppendMode.NoAppend)
    public long EsimID;
}
