package com.bcd.root.support_parser.impl.jtt808.data;

import com.bcd.root.support_parser.anno.F_customize;
import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.impl.jtt808.processor.VehicleControlTypeArrProcessor;

public class VehicleControlRequest implements PacketBody {
    //控制类型数量
    @F_num(type = NumType.uint16, var = 'n')
    public int num;
    //控制类型
    @F_customize(processorClass = VehicleControlTypeArrProcessor.class)
    public VehicleControlType[] types;
}
