package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.impl.jtt808.processor.AlarmPositionExtArrProcessor;

import java.util.Date;

public class AlarmPosition implements PacketBody{
    //报警标志
    @F_num(type = NumType.uint32)
    public long flag;

    //位置信息
    @F_bean
    public Position position;

    @F_customize(processorClass = AlarmPositionExtArrProcessor.class)
    public AlarmPositionExt[] exts;
}
