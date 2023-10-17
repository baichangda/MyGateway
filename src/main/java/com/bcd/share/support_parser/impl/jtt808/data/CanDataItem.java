package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.F_num_array;
import com.bcd.share.support_parser.anno.NumType;

public class CanDataItem {
    //数据项个数
    @F_num(type = NumType.uint32)
    public long id;
    //can数据
    @F_num_array(singleType = NumType.uint8, len = 8)
    public byte[] data;
}
