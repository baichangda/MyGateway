package com.bcd.share.support_parser.impl.icd.data;

import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.F_num_array;
import com.bcd.share.support_parser.anno.NumType;

public class Msg_tailer {
    @F_num(type = NumType.uint32)
    public long check_sum;
    @F_num_array(len = 4, singleType = NumType.uint8)
    public byte[] tail;
}
