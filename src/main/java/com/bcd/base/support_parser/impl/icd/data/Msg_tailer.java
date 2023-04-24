package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_integer_array;

public class Msg_tailer {
    @F_integer(len = 4)
    public long check_sum;
    @F_integer_array(len = 4, singleLen = 1)
    public byte[] tail;
}
