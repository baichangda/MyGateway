package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_num_array;

public class Msg_tailer {
    @F_num(len = 4)
    public long check_sum;
    @F_num_array(len = 4, singleLen = 1)
    public byte[] tail;
}
