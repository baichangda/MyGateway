package com.bcd.share.support_parser.impl.jtt808.data;


import com.bcd.share.support_parser.anno.F_bean;
import com.bcd.share.support_parser.anno.F_customize;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.NumType;
import com.bcd.share.support_parser.impl.jtt808.processor.AlarmPositionExtArrProcessor;

public class AlarmPosition implements PacketBody {
    //报警标志
    @F_num(type = NumType.uint32)
    public long flag;

    //位置信息
    @F_bean
    public Position position;

    @F_customize(processorClass = AlarmPositionExtArrProcessor.class)
    public AlarmPositionExt[] exts;
}
