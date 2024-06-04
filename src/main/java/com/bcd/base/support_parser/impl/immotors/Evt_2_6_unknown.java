package com.bcd.base.support_parser.impl.immotors;

import com.bcd.base.support_parser.anno.F_num_array;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;

public class Evt_2_6_unknown extends Evt_2_6 {
    @F_num_array(len = 6, singleType = NumType.uint8)
    public byte[] data;
}
