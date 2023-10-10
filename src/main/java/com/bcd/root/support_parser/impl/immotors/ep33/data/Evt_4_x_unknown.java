package com.bcd.root.support_parser.impl.immotors.ep33.data;


import com.bcd.root.support_parser.anno.F_num_array;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.impl.immotors.Evt_4_x;

public class Evt_4_x_unknown extends Evt_4_x {
    @F_num_array(lenExpr = "z", singleType = NumType.int8)
    public byte[] data;
}
