package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;
import com.bcd.base.support_parser.impl.immotors.ep33.processor.Evt_D00B_BMSCellVol_processor;

public class Evt_D00B extends Evt_4_x {
    @F_num(len = 1)
    public short BMSCellVolSumNum;
    @F_customize(processorClass = Evt_D00B_BMSCellVol_processor.class)
    public float[] BMSCellVol;
    public byte[] BMSCellVolV;

}
