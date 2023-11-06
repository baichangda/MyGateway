package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.F_num_array;
import com.bcd.share.support_parser.anno.NumType;
import com.bcd.share.support_parser.impl.immotors.Evt_4_x;

public class Evt_4_x_unknown extends Evt_4_x {
    @F_num_array(lenExpr = "z", singleType = NumType.uint8)
    public byte[] data;
}
