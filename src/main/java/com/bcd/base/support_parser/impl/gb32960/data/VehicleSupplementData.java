package com.bcd.base.support_parser.impl.gb32960.data;


import com.bcd.base.support_parser.anno.DateMode;
import com.bcd.base.support_parser.anno.F_customize;
import com.bcd.base.support_parser.anno.F_date;
import com.bcd.base.support_parser.impl.gb32960.processor.VehicleCommonDataFieldProcessor;

import java.util.Date;

public class VehicleSupplementData implements PacketData {
    //数据采集时间
    @F_date(mode = DateMode.bytes_yyMMddHHmmss)
    public Date collectTime;

    //车辆运行通用数据
    @F_customize(processorClass = VehicleCommonDataFieldProcessor.class)
    public VehicleCommonData vehicleCommonData;
}
